package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.network.*;

import java.util.concurrent.BlockingQueue;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.GREEN;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;
import static java.text.MessageFormat.format;

/**
 * Handles messages received from the server by updating controller data and firing the appropriate events.
 */
public class MessageHandler implements Runnable {
  Controller controller;
  BlockingQueue<MessageQueueEntry> messageQueue;

  public MessageHandler(Controller controller, BlockingQueue<MessageQueueEntry> messageQueue) {
    this.controller = controller;
    this.messageQueue = messageQueue;
  }

  /**
   * Runs the message handler loop. <p>
   * Loops over the queue in a blocking way (waiting for messages when empty) and handles them.
   * <p>
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    while (true) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        clientLogger.trace("Handling entry: {}", entry);
        handleMessage(entry);
      } catch (InterruptedException e) {
        // We should never be interrupted
        throw new AssertionError(e);
      }
    }
  }

  /**
   * Handles a {@link MessageQueueEntry} by checking its validity and handling it according to its type.
   *
   * @param entry The {@link MessageQueueEntry} to handle
   */
  private void handleMessage(MessageQueueEntry entry) {
    Client client = entry.client();
    Message message = entry.message();

    if (message.type() == null) {
      clientLogger.warn("Received a message with an invalid message type: {}", message);
      return;
    }

    // Handle PING messages separately to avoid spamming debug logs
    if (message.type() == MessageType.PING) {
      handlePing(client, message);
      return;
    }

    clientLogger.info("Handling entry: {}", entry);
    switch (message.type()) {
      case NICKNAME_OK -> handleNicknameOk(client, message);
      case GAMELIST -> handleGameList(client, message);

      case GAMEINFO -> handleGameInfo(client, message);
      case START_GAME -> handleStartGame(client, message);
      case START_GAME_RECONNECTED -> handleStartGameReconnected(client, message);
      case GAMEDATA -> handleGameData(client, message);

      case PLAYER_DISCONNECTED -> handlePlayerDisconnected(client, message);
      case PLAYER_RECONNECTED -> handlePlayerReconnected(client, message);

      case END_GAME -> handleEndGame(client, message);

      case ERROR -> handleError(client, message);

      case INTERNAL_SOCKET_ERROR -> handleSocketError(client, message);
    }
  }

  /**
   * Handles a {@link MessageType#PING PING} message.
   */
  private void handlePing(Client client, Message message) {
    client.send(new Message.Builder(MessageType.PONG)
        .nickname(controller.getNickname())
        .build());
  }

  /**
   * Handles a {@link MessageType#NICKNAME_OK NICKNAME_OK} message.
   */
  private void handleNicknameOk(Client client, Message message) {
    controller.setNickname(message.nickname());
    controller.fireChange(NICKNAME_OK, null, message.gameInfo());
  }

  /**
   * Handles a {@link MessageType#GAMELIST GAMELIST} message.
   */
  private void handleGameList(Client client, Message message) {
    GameListMessage gameListMessage = (GameListMessage) message;
    controller.setJoinableGameList(gameListMessage.gameList());
    controller.fireChange(GAMELIST, null, gameListMessage.gameList());
  }

  /**
   * Handles a {@link MessageType#GAMEINFO GAMEINFO} message.
   */
  private void handleGameInfo(Client client, Message message) {
    controller.setGameInfo(message.gameInfo());
    controller.setGameCode(message.gameCode());
    controller.fireChange(GAMEINFO_EVENT, null, message.gameInfo());
  }

  /**
   * Handles a {@link MessageType#START_GAME START_GAME} message.
   */
  private void handleStartGame(Client client, Message message) {
    controller.setGameInfo(message.gameInfo());
    controller.initGame();
    controller.executeAction(message.gameAction());
    controller.fireChange(START_GAME, null, message.gameAction());
  }

  /**
   * Handles a {@link MessageType#START_GAME_RECONNECTED START_GAME_RECONNECTED} message.
   */
  private void handleStartGameReconnected(Client client, Message message) {
    controller.setGameCode(message.gameCode());
    controller.setGameInfo(message.gameInfo());
    controller.initEmptyGame();
    controller.executeAction(message.gameAction());
    controller.fireChange(START_GAME, null, message.gameAction());
  }

  /**
   * Handles a {@link MessageType#GAMEDATA GAMEDATA} message.
   */
  private void handleGameData(Client client, Message message) {
    controller.executeAction(message.gameAction());

    // Notifies listeners that the game state was modified
    controller.fireChange(GAMEDATA_EVENT, null, message.gameAction().getDescription());
  }

  /**
   * Handles a {@link MessageType#PLAYER_DISCONNECTED PLAYER_DISCONNECTED} message.
   */
  private void handlePlayerDisconnected(Client client, Message message) {
    controller.setPlayerConnection(false, message.nickname());
    String evtMsg = colored(format("Player \"{0}\" disconnected", message.nickname()), RED);
    controller.fireChange(PLAYER_CONNECTION_CHANGED, null, evtMsg);
  }

  /**
   * Handles a {@link MessageType#PLAYER_RECONNECTED PLAYER_RECONNECTED} message.
   */
  private void handlePlayerReconnected(Client client, Message message) {
    try {
      controller.setPlayerConnection(true, message.nickname());
      String evtMsg = colored(format("Player \"{0}\" reconnected", message.nickname()), GREEN);
      controller.fireChange(PLAYER_CONNECTION_CHANGED, null, evtMsg);
    } catch (NullPointerException e) {
      clientLogger.debug("Tried to reconnect player before the game state was initialized");
    }
  }

  /**
   * Handles a {@link MessageType#END_GAME END_GAME} message.
   */
  private void handleEndGame(Client client, Message message) {
    controller.getGameState().setGamePhase(GamePhase.WIN);
    controller.fireChange(END_GAME, null, null);
  }

  /**
   * Handles a {@link MessageType#ERROR ERROR} message.
   */
  private void handleError(Client client, Message message) {
    controller.showError(message.error());
    controller.fireChange(ERROR, null, null);
  }

  /**
   * Handles a {@link MessageType#INTERNAL_SOCKET_ERROR INTERNAL_SOCKET_ERROR} message.
   */
  private void handleSocketError(Client client, Message message) {
    // Check that this message was created internally and is not coming from the network
    if (!message.nickname().equals(Client.SOCKET_ERROR_HASH))
      return;

    String errorMessage = "\nLost connection to the server";
    if (message.error() != null)
      errorMessage += ": " + message.error();

    client.close();
    controller.fireChange(INTERNAL_SOCKET_ERROR, null, errorMessage);
  }
}
