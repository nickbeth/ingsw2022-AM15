package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.actions.InitiateGameEntities;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;
import org.tinylog.Logger;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServer implements Runnable {
  private static final int HEARTBEAT_INTERVAL_SECONDS = 2;
  private static final int HEARTBEAT_DISCONNECTION_THRESHOLD = 5;

  private final BlockingQueue<MessageQueueEntry> messageQueue;
  private final ScheduledExecutorService heartbeatService;

  private final HashMap<GameCode, GameEntry> activeGames;
  private final Set<String> activeNicknames;
  private final Map<String, GameCode> disconnectedPlayers;

  private final AtomicBoolean exit = new AtomicBoolean(false);

  public GameServer(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
    this.heartbeatService = Executors.newScheduledThreadPool(1);
    this.activeGames = new HashMap<>();
    this.activeNicknames = new HashSet<>();
    this.disconnectedPlayers = new HashMap<>();
  }

  /**
   * Runs the game server loop.
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    while (!exit.get()) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        Logger.trace("Handling entry: {}", entry);
        handleMessage(entry);
      } catch (InterruptedException ignored) {
      }
    }
  }

  private void handleMessage(MessageQueueEntry entry) {
    Client client = entry.client();
    Message message = entry.message();

    if (message == null) {
      Logger.warn("Received a 'null' message");
      return;
    }

    if (message.type() == null) {
      Logger.warn("Received a message with an invalid message type: {}", message);
      return;
    }
    if (message.nickname() == null) {
      Logger.warn("Received a message with an empty nickname: {}", message);
      return;
    }

    // Handle PONG messages separately to avoid spamming debug logs
    if (message.type() == MessageType.PONG) {
      handlePong(client, message);
      return;
    }

    Logger.debug("Handling entry: {}", entry);
    switch (message.type()) {
      case NICKNAME_REQUEST -> handleNicknameRequest(client, message);

      case CREATE_GAME -> handleCreateGame(client, message);
      case JOIN_GAME -> handleJoinGame(client, message);
      case SELECT_TOWER -> handleSelectTower(client, message);

      case START_GAME -> handleStartGame(client, message);

      case PLAY_ACTION -> handlePlayAction(client, message);
    }
  }

  private void handlePong(Client client, Message message) {
    var attachment = (ClientAttachment) client.attachment();
    attachment.resetMissedHeartbeatCount();
  }

  private void handleNicknameRequest(Client client, Message message) {
    String nickname = message.nickname();

    if (nickname == null || nickname.isBlank()) {
      String errorMessage = "Nickname '" + nickname + " is invalid";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check if a player with this nickname already exists, and add it if it doesn't
    if (!activeNicknames.add(nickname)) {
      String errorMessage = "Nickname '" + nickname + "' is already in use";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    client.attach(new ClientAttachment(nickname));
    Logger.info("Nickname '{}' registered for client '{}'", nickname, client);
    send(client, new Message.Builder().type(MessageType.NICKNAME_OK).nickname(nickname).build());

    handleRejoinGame(client, message);
  }

  private void handleRejoinGame(Client client, Message message) {
    String nickname = message.nickname();

    GameCode gameCode = disconnectedPlayers.get(nickname);
    if (gameCode != null) {
      GameEntry gameEntry = activeGames.get(gameCode);

      gameEntry.reconnectPlayer(nickname, client);
      ((ClientAttachment) client.attachment()).setGameCode(gameCode);
      disconnectedPlayers.remove(nickname);

      Logger.info("Player '{}' reconnected to game '{}'", nickname, gameCode);
      // TODO: send game state to the reconnected client
      broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_RECONNECTED).nickname(nickname).build());
      initHeartbeat(client);
    }
  }

  private void handleCreateGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = GameCode.generateUnique(activeGames.keySet());

    var attachment = (ClientAttachment) client.attachment();
    if (attachment == null) {
      String errorMessage = "Nickname '" + nickname + "' should register first before creating a game";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    attachment.setGameCode(gameCode);
    GameEntry gameEntry = new GameEntry(message.gameInfo());
    gameEntry.addPlayer(nickname, client);
    activeGames.put(gameCode, gameEntry);

    Logger.info("Player '{}' created a new game: {}", nickname, gameCode);
    send(client, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
    initHeartbeat(client);
  }

  private void handleJoinGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    if (gameEntry == null) {
      String errorMessage = "Game with code '" + gameCode + "' does not exist";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.getGameInfo().isStarted()) {
      String errorMessage = "Game with code '" + gameCode + "' has already started";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.isFull()) {
      String errorMessage = "Game with code '" + gameCode + "' is full";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    var attachment = (ClientAttachment) client.attachment();
    if (attachment == null) {
      String errorMessage = "Nickname '" + nickname + "' should register first before joining a game";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    attachment.setGameCode(gameCode);
    gameEntry.addPlayer(nickname, client);

    Logger.info("Player '{}' joined game: {}", nickname, gameCode);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
    initHeartbeat(client);
  }

  private void handleSelectTower(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    TowerColor chosenTowerColor = message.gameInfo().getPlayerColor(nickname);
    if (!gameEntry.getGameInfo().isTowerColorValid(nickname, chosenTowerColor)) {
      String errorMessage = "Tower color '" + chosenTowerColor + "' is not available";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.setPlayerColor(nickname, chosenTowerColor);
    Logger.info("'{}' set to tower color: {}", nickname, chosenTowerColor);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleStartGame(Client client, Message message) {
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    gameEntry.initPlayers();
    GameAction action = message.gameAction();

    if (message.gameAction() == null) {
      String errorMessage = "Game with code '" + gameCode + "' received a malformed initialization action";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    if (!(action instanceof InitiateGameEntities) || !gameEntry.executeAction(action)) {
      String errorMessage = "Game with code '" + gameCode + "' tried to start a game with an invalid action: " + action.getClass().getSimpleName();
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    // Check if the game is ready and set the game as started
    if (!gameEntry.getGameInfo().start()) {
      String errorMessage = "Game with code '" + gameCode + "' is not ready to be started";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    Logger.info("Game '{}' has started", gameCode);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.START_GAME).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).action(action).build());
  }

  private void handlePlayAction(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    GameAction action = message.gameAction();

    if (!Objects.equals(nickname, gameEntry.getCurrentPlayer())) {
      String errorMessage = "Game with code '" + gameCode + "' received an action from an invalid player " + nickname;
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (message.gameAction() == null) {
      String errorMessage = "Game with code '" + gameCode + "' received a malformed action";
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (!gameEntry.executeAction(action)) {
      String errorMessage = "Game with code '" + gameCode + "' tried to apply an invalid action: " + action.getClass().getSimpleName();
      Logger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    Logger.info("Player '{}' played action: {}", nickname, action.getClass().getSimpleName());
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEDATA).gameCode(gameCode).action(action).build());
  }

  public void handleSocketError(Client client, Message message) {
    // Check that this message was created internally and is not coming from the network
    if (!message.nickname().equals(Client.SOCKET_ERROR_HASH))
      return;
    ClientAttachment attachment = (ClientAttachment) client.attachment();
    if (attachment != null)
      activeNicknames.remove(attachment.nickname());
    client.close();
  }

  /**
   * Sends a message to the given client and logs the message.
   *
   * @param client  The client to send the message to
   * @param message The message to send
   */
  private void send(Client client, Message message) {
    Logger.debug("Sending message: {}", message);
    client.send(message);
  }

  /**
   * Sends a message to all clients in the given lobby.
   *
   * @param gameEntry The lobby to broadcast to
   * @param message   The message to broadcast
   */
  private void broadcastMessage(GameEntry gameEntry, Message message) {
    Logger.debug("Broadcasting message to {} clients: '{}'", gameEntry.getClients().size(), message);
    gameEntry.getClients().forEach(client -> client.send(message));
  }

  private void initHeartbeat(Client client) {
    heartbeatService.schedule(new Runnable() {
      @Override
      public void run() {
        var attachment = (ClientAttachment) client.attachment();
        if (attachment.increaseMissedHeartbeatCount() > HEARTBEAT_DISCONNECTION_THRESHOLD) {
          GameEntry gameEntry = activeGames.get(attachment.gameCode());
          // If game entry is null, ignore
          if (gameEntry == null)
            return;

          if (!gameEntry.isStarted()) {
            // If the game is not started, remove the client from the lobby
            // If there's only one client in the lobby, remove the game
            activeNicknames.remove(attachment.nickname());
            if (gameEntry.getClients().size() == 1) {
              activeGames.remove(attachment.gameCode());
              Logger.info("Player '{}' left game '{}' while being alone, the game was deleted", attachment.nickname(), attachment.gameCode());
            } else {
              gameEntry.removePlayer(attachment.nickname());
              Logger.info("Player '{}' left game '{}'", attachment.nickname(), attachment.gameCode());
            }
          } else {
            // If the game is started, set the client as disconnected
            gameEntry.disconnectPlayer(attachment.nickname());
            disconnectedPlayers.put(attachment.nickname(), attachment.gameCode());
            Logger.info("Player '{}' playing game '{}' marked as disconnected", attachment.nickname(), attachment.gameCode());
            broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_DISCONNECTED).nickname(attachment.nickname()).build());
          }
          client.attach(null);
          client.close();
          return;
        }

        // Send another ping and re-schedule only if threshold was not reached
        client.send(new Message.Builder().type(MessageType.PING).build());
        heartbeatService.schedule(this, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
      }
    }, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
  }

  /**
   * Sets the server exit flag.
   * After this has been called, the server should be considered stopped.
   * No guarantees are made that this call will stop the server immediately.
   */
  public void exit() {
    exit.set(true);
  }
}
