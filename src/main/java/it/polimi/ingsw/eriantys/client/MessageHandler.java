package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.util.concurrent.BlockingQueue;

import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class MessageHandler implements Runnable {
  Controller controller;
  BlockingQueue<MessageQueueEntry> messageQueue;

  public MessageHandler(Controller controller, BlockingQueue<MessageQueueEntry> messageQueue) {
    this.controller = controller;
    this.messageQueue = messageQueue;
  }

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
      case GAMEINFO -> handleGameInfo(client, message);
      case START_GAME -> handleStartGame(client, message);
      case GAMEDATA -> handleGameData(client, message);

      case PLAYER_DISCONNECTED -> handlePlayerDisconnected(client, message);
      case PLAYER_RECONNECTED -> handlePlayerReconnected(client, message);

      case ERROR -> handleError(client, message);

      case INTERNAL_SOCKET_ERROR -> handleSocketError(client, message);
    }
  }

  private void handlePing(Client client, Message message) {
    client.send(new Message.Builder(MessageType.PONG)
        .nickname(controller.getNickname())
        .build());
  }

  private void handleNicknameOk(Client client, Message message) {
    controller.setNickname(message.nickname());
    controller.fireChange(NICKNAME_OK, null, null);
  }

  private void handleGameInfo(Client client, Message message) {
    controller.setGameInfo(message.gameInfo());
    controller.setGameCode(message.gameCode());
    controller.fireChange(GAMEINFO_EVENT, null, message.gameInfo());
  }

  private void handleStartGame(Client client, Message message) {
    controller.setGameInfo(message.gameInfo());
    controller.initGame();
    controller.executeAction(message.gameAction());
    controller.fireChange(START_GAME, null, message.gameAction());
  }

  private void handleGameData(Client client, Message message) {
    controller.executeAction(message.gameAction());

    // Notifies listeners that the game state was modified
    controller.fireChange(GAMEDATA_EVENT, null, message.gameAction().getDescription());
  }

  private void handlePlayerDisconnected(Client client, Message message) {
    controller.setPlayerConnected(false, message.nickname());
    controller.fireChange(PLAYER_CONNECTION_CHANGED, null, null);
  }

  private void handlePlayerReconnected(Client client, Message message) {
    try {
      controller.setPlayerConnected(true, message.nickname());
    } catch (NullPointerException e) {
      clientLogger.error("Cannot set reconnect because this game state does not exists");
    }
    controller.fireChange(PLAYER_CONNECTION_CHANGED, null, null);
  }

  private void handleError(Client client, Message message) {
    controller.showError(message.error());
    controller.fireChange(ERROR, null, null);
  }

  private void handleSocketError(Client client, Message message) {
    // Check that this message was created internally and is not coming from the network
    if (!message.nickname().equals(Client.SOCKET_ERROR_HASH))
      return;

    String errorMessage = "Lost connection to the server";
    if (message.error() != null)
      errorMessage += ": " + message.error();

    controller.showNetworkError(errorMessage);
    client.close();
    controller.fireChange(INTERNAL_SOCKET_ERROR, null, null);
  }
}