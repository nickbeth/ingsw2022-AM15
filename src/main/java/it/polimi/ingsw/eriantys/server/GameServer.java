package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.*;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import static it.polimi.ingsw.eriantys.server.Util.generateGameCode;

public class GameServer implements Runnable {
  private final BlockingQueue<MessageQueueEntry> messageQueue;
  private final HashMap<String, GameEntry> activeGames;
  private final Set<String> activeNicknames;

  public GameServer(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
    this.activeGames = new HashMap<>();
    this.activeNicknames = new HashSet<>();
  }

  /**
   * Runs the game server loop.
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    while (true) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        Logger.trace("Handling request: {}", entry);
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
      Logger.warn("Invalid message type: {}", message);
      return;
    }
    if (message.nickname() == null) {
      Logger.warn("Received a message with an empty nickname: {}", message);
      return;
    }

    switch (message.type()) {
      case PING -> handlePing(client, message);
      case PONG -> handlePong(client, message);

      case CREATE_GAME -> handleCreateGame(client, message);
      case JOIN_GAME -> handleJoinGame(client, message);
      case SELECT_TOWER -> handleSelectTower(client, message);

      case START_GAME -> handleStartGame(client, message);

      case PLAY_ACTION -> handlePlayAction(client, message);
      case ERROR -> handleError(client, message);
    }
  }

  private void handlePing(Client client, Message message) {
    client.send(new Message.Builder().type(MessageType.PONG).build());
  }

  private void handlePong(Client client, Message message) {

  }

  private void handleCreateGame(Client client, Message message) {
    String gameCode = generateGameCode();
    if (activeNicknames.contains(message.nickname())) {
      String errorMessage = String.format("Nickname '%s' is already in use", message.nickname());
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    GameEntry gameEntry = new GameEntry(message.gameInfo());
    gameEntry.addPlayer(message.nickname(), client);

    activeGames.put(gameCode, gameEntry);
    activeNicknames.add(message.nickname());
    client.send(new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleJoinGame(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    if (gameEntry == null) {
      String errorMessage = String.format("Game with code '%s' does not exist", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.isFull()) {
      String errorMessage = String.format("Game with code '%s' is full", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING) {
      String errorMessage = String.format("Game with code '%s' has already started", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check if a player with this nickname already exists, and add it if it doesn't
    if (!activeNicknames.add(message.nickname())) {
      String errorMessage = String.format("Nickname '%s' is already in use", message.nickname());
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.addPlayer(message.nickname(), client);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleSelectTower(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    TowerColor chosenTowerColor = message.gameInfo().getPlayerColor(message.nickname());
    gameEntry.setPlayerColor(message.nickname(), chosenTowerColor);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleStartGame(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    if (!gameEntry.getGameInfo().isReady()) {
      String errorMessage = String.format("Game with code '%s' is not ready to be started", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.getGameInfo().start();
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handlePlayAction(Client client, Message message) {
  }

  private void handleError(Client client, Message message) {
  }

  /**
   * Sends a message to all clients in the given lobby.
   *
   * @param gameEntry The lobby to broadcast to
   * @param message   The message to broadcast
   */
  private void broadcastMessage(GameEntry gameEntry, Message message) {
    gameEntry.getClients().forEach(client -> client.send(message));
  }
}
