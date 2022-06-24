package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

public class GameServer implements Runnable {
  private static final int HEARTBEAT_INTERVAL_SECONDS = 2;
  private static final int HEARTBEAT_DISCONNECTION_THRESHOLD = 5;

  /**
   * Whether heartbeat messages should be used to keep track of clients disconnections or not.
   */
  private final boolean heartbeat;
  private final BlockingQueue<MessageQueueEntry> messageQueue;
  private final ScheduledExecutorService heartbeatService;

  private final Map<GameCode, GameEntry> activeGames;
  private final Set<String> activeNicknames;
  private final Map<String, GameCode> disconnectedPlayers;

  private final AtomicBoolean exit = new AtomicBoolean(false);

  public GameServer(boolean heartbeat, BlockingQueue<MessageQueueEntry> messageQueue) {
    this.heartbeat = heartbeat;
    this.messageQueue = messageQueue;
    this.heartbeatService = Executors.newScheduledThreadPool(1);
    this.activeGames = new ConcurrentHashMap<>();
    this.activeNicknames = ConcurrentHashMap.newKeySet();
    this.disconnectedPlayers = new ConcurrentHashMap<>();
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
        serverLogger.trace("Handling entry: {}", entry);
        handleMessage(entry);
      } catch (InterruptedException ignored) {
      }
    }
  }

  private void handleMessage(MessageQueueEntry entry) {
    Client client = entry.client();
    Message message = entry.message();

    if (message == null) {
      serverLogger.warn("Received a 'null' message");
      return;
    }

    if (message.type() == null) {
      serverLogger.warn("Received a message with an invalid message type: {}", message);
      return;
    }
    if (message.nickname() == null) {
      serverLogger.warn("Received a message with an empty nickname: {}", message);
      return;
    }

    // Handle PONG messages separately to avoid spamming debug logs
    if (message.type() == MessageType.PONG) {
      handlePong(client, message);
      return;
    }

    serverLogger.debug("Handling entry: {}", entry);
    switch (message.type()) {
      case NICKNAME_REQUEST -> handleNicknameRequest(client, message);

      case CREATE_GAME -> handleCreateGame(client, message);
      case JOIN_GAME -> handleJoinGame(client, message);
      case QUIT_GAME -> handleQuitGame(client, message);
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
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check if a player with this nickname already exists, and add it if it doesn't
    if (!activeNicknames.add(nickname)) {
      String errorMessage = "Nickname '" + nickname + "' is already in use";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    client.attach(new ClientAttachment(nickname));
    serverLogger.info("Nickname '{}' registered for client '{}'", nickname, client);
    send(client, new Message.Builder().type(MessageType.NICKNAME_OK).nickname(nickname).build());
    initHeartbeat(client);

    tryRejoinGame(client, message);
  }

  private boolean tryRejoinGame(Client client, Message message) {
    String nickname = message.nickname();

    GameCode gameCode = disconnectedPlayers.get(nickname);
    if (gameCode != null) {
      GameEntry gameEntry = activeGames.get(gameCode);

      gameEntry.reconnectPlayer(nickname, client);
      ((ClientAttachment) client.attachment()).setGameCode(gameCode);
      disconnectedPlayers.remove(nickname);

      serverLogger.info("Player '{}' reconnected to game '{}'", nickname, gameCode);
      send(client, new Message.Builder().type(MessageType.START_GAME_RECONNECTED)
          .gameCode(gameCode)
          .gameInfo(gameEntry.getGameInfo())
          .action(new ReInitiateGame(gameEntry.getGameState()))
          .build());
      broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_RECONNECTED).nickname(nickname).build());
      return true;
    }
    return false;
  }

  private void handleCreateGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = GameCode.generateUnique(activeGames.keySet());

    var attachment = (ClientAttachment) client.attachment();
    if (attachment == null) {
      String errorMessage = "Nickname '" + nickname + "' should register first before creating a game";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (attachment.gameCode() != null || disconnectedPlayers.containsKey(nickname)) {
      String errorMessage = "Nickname '" + nickname + "' is already in a game";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    attachment.setGameCode(gameCode);
    GameEntry gameEntry = new GameEntry(message.gameInfo());
    gameEntry.addPlayer(nickname, client);
    activeGames.put(gameCode, gameEntry);

    serverLogger.info("Player '{}' created a new game: {}", nickname, gameCode);
    send(client, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleJoinGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    if (gameEntry == null) {
      String errorMessage = "Game with code '" + gameCode + "' does not exist";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.getGameInfo().isStarted()) {
      if (tryRejoinGame(client, message))
        return;

      String errorMessage = "Game with code '" + gameCode + "' has already started";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.isFull()) {
      String errorMessage = "Game with code '" + gameCode + "' is full";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    var attachment = (ClientAttachment) client.attachment();
    if (attachment == null) {
      String errorMessage = "Nickname '" + nickname + "' should register first before joining a game";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    attachment.setGameCode(gameCode);
    gameEntry.addPlayer(nickname, client);

    serverLogger.info("Player '{}' joined game '{}'", nickname, gameCode);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleQuitGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode(); // If the player is not yet in a lobby, this will be null

    if (gameCode == null) {
      // The player was not in a lobby: remove the player's nickname and close the socket
      // Following this, the client is to be considered disconnected from the server
      stopHeartbeat(client);
      activeNicknames.remove(nickname);
      client.attach(null);
      client.close();
      return;
    }

    ClientAttachment attachment = (ClientAttachment) client.attachment();
    GameEntry gameEntry = activeGames.get(gameCode);
    if (!gameEntry.isStarted()) {
      // The player was in a lobby: remove it from the lobby or delete the lobby if last
      // Following this, the client is to be considered connected to the server with heartbeat running and nickname still registered
      if (gameEntry.getClients().size() == 1) {
        activeGames.remove(gameCode);
        serverLogger.info("Player '{}' left game '{}' while being alone, the game was deleted", nickname, gameCode);
      } else {
        gameEntry.removePlayer(nickname);
        serverLogger.info("Player '{}' left game '{}'", nickname, gameCode);
        broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
      }
    } else {
      // The player was playing a game: disconnect it from the game or delete the game if last
      // Following this, the client is to be considered connected to the server with heartbeat running and nickname still registered
      if (gameEntry.getClients().size() == 1) {
        deleteGame(gameCode, gameEntry);
        serverLogger.info("Player '{}' left ongoing game '{}' while being alone, the game was deleted", nickname, gameCode);
      } else {
        disconnectPlayer(gameEntry, nickname);
        disconnectedPlayers.put(nickname, gameCode);
        serverLogger.info("Player '{}' left ongoing game '{}', marked as disconnected", nickname, gameCode);
        broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_DISCONNECTED).nickname(nickname).build());
      }
    }
    // Always clear this player's game code as it's not actively in a game anymore
    attachment.setGameCode(null);
  }

  private void handleSelectTower(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    TowerColor chosenTowerColor = message.gameInfo().getPlayerColor(nickname);
    if (!gameEntry.getGameInfo().isTowerColorValid(nickname, chosenTowerColor)) {
      String errorMessage = "Tower color '" + chosenTowerColor + "' is not available";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.setPlayerColor(nickname, chosenTowerColor);
    serverLogger.info("'{}' set to tower color: {}", nickname, chosenTowerColor);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleStartGame(Client client, Message message) {
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    gameEntry.initPlayers();
    GameAction action = message.gameAction();

    if (message.gameAction() == null) {
      String errorMessage = "Game with code '" + gameCode + "' received a malformed initialization action";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    if (!(action instanceof InitiateGameEntities) || !gameEntry.executeAction(action)) {
      String errorMessage = "Game with code '" + gameCode + "' tried to start a game with an invalid action: " + action.getClass().getSimpleName();
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    // Check if the game is ready and set the game as started
    if (!gameEntry.getGameInfo().start()) {
      String errorMessage = "Game with code '" + gameCode + "' is not ready to be started";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    serverLogger.info("Game '{}' has started", gameCode);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.START_GAME).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).action(action).build());
  }

  private void handlePlayAction(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    GameAction action = message.gameAction();

    if (!Objects.equals(nickname, gameEntry.getCurrentPlayer())) {
      String errorMessage = "'" + nickname + "' played an action '" + action.getClass().getSimpleName() + "' in game '" + gameCode + "' when it wasn't his turn";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (message.gameAction() == null) {
      String errorMessage = "Game with code '" + gameCode + "' received a malformed action";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (!gameEntry.executeAction(action)) {
      String errorMessage = "Game with code '" + gameCode + "' tried to apply an invalid action: " + action.getClass().getSimpleName();
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    serverLogger.info("Player '{}' played action: {}", nickname, action.getClass().getSimpleName());
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEDATA).gameCode(gameCode).action(action).build());

    // Check win condition
    if (gameEntry.checkWinCondition()) {
      Optional<TowerColor> winner = gameEntry.getGameState().getWinner();

      String logMessage = winner.isEmpty() ?
          "Game '" + gameCode + "' has ended in a tie" :
          "Game '" + gameCode + "' has ended with winner: " + winner.get();

      serverLogger.info(logMessage);
      broadcastMessage(gameEntry, new Message.Builder().type(MessageType.END_GAME).gameCode(gameCode).build());
    }
  }

  /**
   * Sends a message to the given client and logs the message.
   *
   * @param client  The client to send the message to
   * @param message The message to send
   */
  private void send(Client client, Message message) {
    serverLogger.debug("Sending message: {}", message);
    client.send(message);
  }

  /**
   * Sends a message to all clients in the given lobby.
   *
   * @param gameEntry The lobby to broadcast to
   * @param message   The message to broadcast
   */
  private void broadcastMessage(GameEntry gameEntry, Message message) {
    serverLogger.debug("Broadcasting message to {} clients: '{}'", gameEntry.getClients().size(), message);
    gameEntry.getClients().forEach(client -> client.send(message));
  }

  /**
   * Disconnects the given player from the given game.
   *
   * @param gameEntry The game to disconnect the player from
   * @param nickname  The nickname of the player to disconnect
   */
  private void disconnectPlayer(GameEntry gameEntry, String nickname) {
    gameEntry.disconnectPlayer(nickname);
    // Send the disconnection message to the client
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_DISCONNECTED).nickname(nickname).build());

    GameState gameState = gameEntry.getGameState();
    // We need special handling if the disconnected player was the last one of the current game phase
    if (gameState.isTurnOf(nickname) && gameState.isLastPlayer(gameState.getPlayer(nickname))) {
      // If the player was the last of the action phase, we need to refill clouds
      if (gameState.getGamePhase() == GamePhase.ACTION) {
        GameAction refillAction = new RefillClouds(gameState);
        gameEntry.executeAction(refillAction);
        broadcastMessage(gameEntry, new Message.Builder()
            .type(MessageType.GAMEDATA)
            .action(refillAction)
            .build()
        );
      }

      // Advance to the next connected player
      GameAction advanceAction = new AdvanceToNextConnectedPlayer();
      gameEntry.executeAction(advanceAction);
      broadcastMessage(gameEntry, new Message.Builder()
          .type(MessageType.GAMEDATA)
          .action(advanceAction)
          .build()
      );
    }
  }

  /**
   * Deletes a game from the active games list and cleans up its related disconnected players
   *
   * @param gameCode  The game code to delete
   * @param gameEntry The game entry to delete
   */
  private void deleteGame(GameCode gameCode, GameEntry gameEntry) {
    // Clean up disconnected players
    GameInfo gameInfo = gameEntry.getGameInfo();
    for (String player : gameInfo.getJoinedPlayers())
      disconnectedPlayers.remove(player);

    activeGames.remove(gameCode);
  }

  /**
   * Initializes the heartbeat for the given client.
   *
   * @param client The client to initialize the heartbeat for
   * @apiNote This method should only be called on a client that has a valid attachment
   */
  private void initHeartbeat(Client client) {
    if (!heartbeat)
      return;

    var attachment = (ClientAttachment) client.attachment();
    serverLogger.debug("Initializing heartbeat for player '{}' on client '{}'", attachment.nickname(), client);
    var heartbeatSchedule = heartbeatService.schedule(new HeartbeatRunnable(client), HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
    // Save the heartbeat schedule in the attachment, so we can cancel it later
    attachment.setHeartbeatSchedule(heartbeatSchedule);
  }

  /**
   * Cancels the heartbeat for the given client.
   *
   * @param client The client to cancel the heartbeat for
   * @apiNote This method should only be called on a client that has a valid attachment
   */
  private void stopHeartbeat(Client client) {
    if (!heartbeat)
      return;

    var attachment = (ClientAttachment) client.attachment();
    serverLogger.debug("Stopping heartbeat for player '{}' on client '{}'", attachment.nickname(), client);
    // We acquire the lock to avoid cancelling the heartbeat while another thread is scheduling a new one,
    // which would result in cancelling the wrong heartbeat schedule
    attachment.acquireHeartbeatLock();
    try {
      attachment.cancelHeartbeatSchedule();
    } finally {
      attachment.releaseHeartbeatLock();
    }
  }

  /**
   * The heartbeat function that will be run for every client once it joins a lobby.
   */
  private class HeartbeatRunnable implements Runnable {
    private final Client client;

    public HeartbeatRunnable(Client client) {
      this.client = client;
    }

    @Override
    public void run() {
      var attachment = (ClientAttachment) client.attachment();
      // Send another ping and re-schedule if threshold was not reached
      if (attachment.increaseMissedHeartbeatCount() <= HEARTBEAT_DISCONNECTION_THRESHOLD) {
        client.send(new Message.Builder().type(MessageType.PING).build());

        // We acquire the lock to avoid scheduling a new heartbeat while another thread is cancelling the old one
        attachment.acquireHeartbeatLock();
        try {
          // Re-schedule only if the heartbeat was not cancelled
          // We want to avoid rescheduling the heartbeat if the last one was cancelled
          if (!attachment.isHeartbeatCancelled()) {
            var heartbeatSchedule = heartbeatService.schedule(this, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
            attachment.setHeartbeatSchedule(heartbeatSchedule);
          }
        } finally {
          attachment.releaseHeartbeatLock();
        }
        return;
      }
      serverLogger.debug("Failed heartbeat threshold reached for player '{}' on client '{}'", attachment.nickname(), client);

      String nickname = attachment.nickname();
      GameCode gameCode = attachment.gameCode();
      GameEntry gameEntry = gameCode != null ? activeGames.get(gameCode) : null;

      // If game entry is null, the player disconnected before joining a lobby or game
      if (gameEntry != null) {
        if (!gameEntry.isStarted()) {
          // If the game has not started, remove the client from the lobby
          // If there was only one client in the lobby, remove the game
          if (gameEntry.getClients().size() == 1) {
            activeGames.remove(gameCode);
            serverLogger.info("Player '{}' lost connection to game '{}' while being alone, the game was deleted", nickname, gameCode);
          } else {
            gameEntry.removePlayer(nickname);
            serverLogger.info("Player '{}' lost connection to game '{}'", nickname, gameCode);
            broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
          }
        } else {
          if (!disconnectedPlayers.containsKey(nickname)) {
            // If the game has started, set the client as disconnected
            disconnectPlayer(gameEntry, nickname);
            disconnectedPlayers.put(nickname, gameCode);
            serverLogger.info("Player '{}' lost connection to ongoing game '{}', marked as disconnected", nickname, gameCode);
          } else {
            // The player had already disconnected with a QUIT_GAME message, skip marking as disconnected
            serverLogger.info("Player '{}' lost connection while already disconnected from ongoing game '{}'", nickname, gameCode);
          }
        }
      }

      activeNicknames.remove(nickname);
      client.attach(null);
      client.close();
    }
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
