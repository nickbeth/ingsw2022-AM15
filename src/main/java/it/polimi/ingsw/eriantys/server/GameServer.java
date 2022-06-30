package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.*;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

/**
 * The game server class contains the logic for handling clients and game messages.
 */
public class GameServer implements Runnable {
  private static final int HEARTBEAT_INTERVAL_SECONDS = 2;
  private static final int HEARTBEAT_DISCONNECTION_THRESHOLD = 5;

  /**
   * See {@link ServerArgs#heartbeat heartbeat}
   */
  private final boolean heartbeat;

  /**
   * See {@link ServerArgs#deleteTimeout deleteTimeout}
   */
  private final int deleteTimeout;

  /**
   * The queue where incoming messages from clients are pushed to
   */
  private final BlockingQueue<MessageQueueEntry> messageQueue;

  /**
   * A thread pool for taking care of scheduled background tasks
   */
  private final ScheduledExecutorService scheduledExecutorService;

  /**
   * A map from a {@link GameCode} to a {@link GameEntry}, representing all the games currently active on the server
   */
  private final GameList activeGames;

  /**
   * A set of all active nicknames on the server
   */
  private final Set<String> activeNicknames;

  /**
   * A map from a player's name to the {@link GameCode} of the game the player has disconnected from
   */
  private final Map<String, GameCode> disconnectedPlayers;

  /**
   * The exit flag for the game server thread. Used for instrumentation purposes.
   */
  private final AtomicBoolean exit = new AtomicBoolean(false);

  public GameServer(boolean heartbeat, int deleteTimeout, BlockingQueue<MessageQueueEntry> messageQueue) {
    this.heartbeat = heartbeat;
    this.deleteTimeout = deleteTimeout;
    this.messageQueue = messageQueue;
    this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    this.activeGames = new GameList();
    this.activeNicknames = ConcurrentHashMap.newKeySet();
    this.disconnectedPlayers = new ConcurrentHashMap<>();
  }

  /**
   * Runs the game server loop. <p>
   * Loops over the queue in a blocking way (waiting for messages when empty) and handles them.
   * <p>
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

  /**
   * Handles a {@link MessageQueueEntry} by checking its validity and handling it according to its type.
   *
   * @param entry The {@link MessageQueueEntry} to handle
   */
  private void handleMessage(MessageQueueEntry entry) {
    Client client = entry.client();
    Message message = entry.message();

    if (message == null) {
      serverLogger.warn("Received a 'null' message");
      return;
    }

    // Check that the message is of valid type
    if (message.type() == null) {
      serverLogger.warn("Received a message of invalid type: {}", message);
      return;
    }

    // Check that the message contains a valid nickname for all types of messages
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
      case NICKNAME_REQUEST -> {
        handleNicknameRequest(client, message);
        return;
      }

      case CREATE_GAME -> {
        handleCreateGame(client, message);
        return;
      }

      case GAMELIST_REQUEST -> {
        handleGamelistRequest(client, message);
        return;
      }
    }

    // Check that the message contains a valid game code only for the types of messages that require it
    if (message.gameCode() == null) {
      serverLogger.warn("Received a message that requires a valid game code but didn't have one: {}", message);
      return;
    }

    switch (message.type()) {
      case JOIN_GAME -> handleJoinGame(client, message);
      case QUIT_GAME -> handleQuitGame(client, message);
      case SELECT_TOWER -> handleSelectTower(client, message);

      case START_GAME -> handleStartGame(client, message);

      case PLAY_ACTION -> handlePlayAction(client, message);
    }
  }

  /**
   * Handles a {@link MessageType#PONG PONG} message.
   */
  private void handlePong(Client client, Message message) {
    var attachment = (ClientAttachment) client.attachment();
    attachment.resetMissedHeartbeatCount();
  }

  /**
   * Handles a {@link MessageType#NICKNAME_REQUEST NICKNAME_REQUEST} message.
   */
  private void handleNicknameRequest(Client client, Message message) {
    String nickname = message.nickname();

    // Check that the provided nickname is valid
    if (nickname.isBlank()) {
      String errorMessage = "Nickname '" + nickname + " is invalid";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Try to add the requested nickname and check that it was added successfully
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

  /**
   * Handles a {@link MessageType#GAMELIST_REQUEST GAMELIST_REQUEST} message.
   */
  private void handleGamelistRequest(Client client, Message message) {
    send(client, new GameListMessage.Builder().gameList(activeGames.getJoinableGameList()).build());
    serverLogger.info("Sent game list to client '{}'", message.nickname());
  }

  /**
   * Handles reconnection to a game, either after a
   * {@link MessageType#NICKNAME_REQUEST NICKNAME_REQUEST} or a
   * {@link MessageType#JOIN_GAME JOIN_GAME} message.
   *
   * @return {@code true} if the client was successfully reconnected to a game, {@code false} otherwise
   */
  private boolean tryRejoinGame(Client client, Message message) {
    String nickname = message.nickname();

    GameCode gameCode = disconnectedPlayers.get(nickname);
    if (gameCode != null) {
      GameEntry gameEntry = activeGames.get(gameCode);
      if (gameEntry.getClients().size() == 1)
        gameEntry.cancelDeletion();

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

  /**
   * Handles a {@link MessageType#CREATE_GAME CREATE_GAME} message.
   */
  private void handleCreateGame(Client client, Message message) {
    String nickname = message.nickname();

    var attachment = (ClientAttachment) client.attachment();
    if (attachment == null) {
      String errorMessage = "Nickname '" + nickname + "' should register first before creating a game";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the player is not already in a game
    if (attachment.gameCode() != null || disconnectedPlayers.containsKey(nickname)) {
      String errorMessage = "Nickname '" + nickname + "' is already in a game";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    Pair<GameCode, GameEntry> gameEntryPair = activeGames.create(message.gameInfo());
    GameCode gameCode = gameEntryPair.getValue0();
    GameEntry gameEntry = gameEntryPair.getValue1();

    gameEntry.addPlayer(nickname, client);
    attachment.setGameCode(gameCode);

    serverLogger.info("Player '{}' created a new game: {}", nickname, gameCode);
    send(client, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  /**
   * Handles a {@link MessageType#JOIN_GAME JOIN_GAME} message.
   */
  private void handleJoinGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    // Check that the game exists
    if (gameEntry == null) {
      String errorMessage = "Game with code '" + gameCode + "' does not exist";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the game has not started yet
    if (gameEntry.getGameInfo().isStarted()) {
      // Try to reconnect the player to the game if it has started
      if (tryRejoinGame(client, message))
        return;

      String errorMessage = "Game with code '" + gameCode + "' has already started";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the game is not full already
    if (gameEntry.isFull()) {
      String errorMessage = "Game with code '" + gameCode + "' is full";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the client has a valid attachment
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

  /**
   * Handles a {@link MessageType#QUIT_GAME QUIT_GAME} message.
   */
  private void handleQuitGame(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode(); // If the player is not yet in a lobby, this will be null

    handleClientRemoval(nickname, gameCode, false);

    if (gameCode == null) {
      // The player was not in a lobby: remove the player's nickname and close the socket
      // Following this, the client is to be considered disconnected from the server
      stopHeartbeat(client);
      activeNicknames.remove(nickname);
      client.attach(null);
      client.close();
    } else {
      // Clear this player's game code as it's not actively in a game anymore
      ((ClientAttachment) client.attachment()).setGameCode(null);
    }
  }

  /**
   * Handles a {@link MessageType#SELECT_TOWER SELECT_TOWER} message.
   */
  private void handleSelectTower(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameInfo gameInfo = message.gameInfo();
    GameEntry gameEntry = activeGames.get(gameCode);

    // Check that the game info in the message is valid
    if (gameInfo == null) {
      String errorMessage = "Player '" + nickname + "' in game '" + gameCode + "' tried to select a tower color with a malformed gameinfo";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the chosen tower color is valid
    TowerColor chosenTowerColor = gameInfo.getPlayerColor(nickname);
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

  /**
   * Handles a {@link MessageType#START_GAME START_GAME} message.
   */
  private void handleStartGame(Client client, Message message) {
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    gameEntry.initPlayers();
    GameAction action = message.gameAction();

    // Check that the action in the message is valid
    if (action == null) {
      String errorMessage = "Game with code '" + gameCode + "' received a malformed initialization action";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check if the game is ready and set the game as started
    if (!gameEntry.getGameInfo().isReady()) {
      String errorMessage = "Game with code '" + gameCode + "' is not ready to be started";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the initialization action is of the correct type and try to execute it
    if (!(action instanceof InitiateGameEntities) || !gameEntry.executeAction(action)) {
      String errorMessage = "Game with code '" + gameCode + "' tried to start a game with an invalid action: " + action.getClass().getSimpleName();
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Set the game as started only once the initialization action has been executed successfully
    gameEntry.start();
    serverLogger.info("Game '{}' has started", gameCode);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.START_GAME).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).action(action).build());
  }

  /**
   * Handles a {@link MessageType#PLAY_ACTION PLAY_ACTION} message.
   */
  private void handlePlayAction(Client client, Message message) {
    String nickname = message.nickname();
    GameCode gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    GameAction action = message.gameAction();

    // Check that the action in the message is valid
    if (action == null) {
      String errorMessage = "Player '" + nickname + "' in game '" + gameCode + "' sent a malformed action";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the player can play the action
    if (!Objects.equals(nickname, gameEntry.getCurrentPlayer())) {
      String errorMessage = "'" + nickname + "' played action '" + action.getClass().getSimpleName() + "' in game '" + gameCode + "' when it wasn't his turn";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check that the player is not alone in the game
    if (gameEntry.getClients().size() == 1) {
      String errorMessage = "Player '" + nickname + "' in game '" + gameCode + "' tried to play an action while only one player is left";
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Try to apply the received action
    if (!gameEntry.executeAction(action)) {
      String errorMessage = "Player '" + nickname + "' in game '" + gameCode + "' tried to apply an invalid action: " + action.getClass().getSimpleName();
      serverLogger.info(errorMessage);
      send(client, new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    serverLogger.info("Player '{}' played action: {}", nickname, action.getClass().getSimpleName());
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEDATA).gameCode(gameCode).action(action).build());

    // Check win condition and delete the game if it has been reached
    if (gameEntry.checkWinCondition()) {
      Optional<TowerColor> winner = gameEntry.getGameState().getWinner();

      String logMessage = winner.isEmpty() ?
          "Game '" + gameCode + "' has ended in a tie" :
          "Game '" + gameCode + "' has ended with winner: " + winner.get();

      serverLogger.info(logMessage);
      broadcastMessage(gameEntry, new Message.Builder().type(MessageType.END_GAME).gameCode(gameCode).build());
      deleteGame(gameCode, gameEntry);
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
    GameState gameState = gameEntry.getGameState();
    boolean disconnectedPlayerWasLast = gameState.isLastPlayer(gameState.getPlayer(nickname));

    gameEntry.disconnectPlayer(nickname);
    // Send the disconnection message to the client
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.PLAYER_DISCONNECTED).nickname(nickname).build());

    // We need special handling if the disconnected player was the current player
    if (gameState.isTurnOf(nickname)) {
      // If the player was the last of the action phase, we need to refill clouds
      if (gameState.getGamePhase() == GamePhase.ACTION && disconnectedPlayerWasLast) {
        GameAction refillAction = new RefillClouds(gameState);
        gameEntry.executeAction(refillAction);
        broadcastMessage(gameEntry, new Message.Builder()
            .type(MessageType.GAMEDATA)
            .action(refillAction)
            .build()
        );
      }

      // Advance to the next connected player
      GameAction advanceAction = new AdvanceState();
      gameEntry.executeAction(advanceAction);
      broadcastMessage(gameEntry, new Message.Builder()
          .type(MessageType.GAMEDATA)
          .action(advanceAction)
          .build()
      );
    }
  }

  /**
   * Handles removal of a player from a game.
   *
   * @param nickname        The nickname of the player to remove
   * @param gameCode        The game code of the game to remove the player from
   * @param heartbeatFailed Whether the player was removed because of a heartbeat failure or not
   */
  private void handleClientRemoval(String nickname, GameCode gameCode, boolean heartbeatFailed) {
    // Save the action to print to logs
    String logAction = heartbeatFailed ? "lost connection to" : "left";

    // The player was not in any game
    if (gameCode == null) {
      serverLogger.info("Player '{}' {} the server", nickname, logAction);
      return;
    }

    GameEntry gameEntry = activeGames.get(gameCode);
    // The game doesn't exist, warn and ignore
    if (gameEntry == null) {
      serverLogger.warn("Player '{}' {} game '{}' that doesn't exist anymore", nickname, logAction, gameCode);
      return;
    }

    if (!gameEntry.isStarted()) {
      // The player was in a lobby: remove it from the lobby or delete the lobby if last
      if (gameEntry.getClients().size() == 1) {
        activeGames.remove(gameCode);
        serverLogger.info("Player '{}' {} game '{}' while being alone, the game was deleted", nickname, logAction, gameCode);
      } else {
        gameEntry.removePlayer(nickname);
        serverLogger.info("Player '{}' {} game '{}'", nickname, logAction, gameCode);
        broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
      }
    } else {
      // The player was playing a game: disconnect it from the game or delete the game if last
      int connectedClients = gameEntry.getClients().size();

      // If the player is the last one in the game, delete the game
      if (connectedClients == 1) {
        gameEntry.cancelDeletion();
        deleteGame(gameCode, gameEntry);
        serverLogger.info("Player '{}' {} ongoing game '{}' while being alone, the game was deleted", nickname, logAction, gameCode);
        return;
      }

      // If the player is the second-last, schedule the game to be deleted after the deletion interval
      if (connectedClients == 2) {
        // Get the winner so that the scheduled lambda can read it later
        // Since the player is the second-last, the winner will be the other player in the game
        String winner = gameEntry.getClientNames().stream().filter(name -> !name.equals(nickname)).findFirst().orElse("");

        ScheduledFuture<?> deletionSchedule = scheduledExecutorService.schedule(() -> {
          deleteGame(gameCode, gameEntry);
          broadcastMessage(gameEntry, new Message.Builder().type(MessageType.END_GAME).gameCode(gameCode).build());
          serverLogger.info("Player '{}' won game '{}' as the last one standing, the game was deleted", winner, gameCode);
        }, deleteTimeout, TimeUnit.SECONDS);
        gameEntry.setDeletionSchedule(deletionSchedule);
      }

      disconnectPlayer(gameEntry, nickname);
      disconnectedPlayers.put(nickname, gameCode);
      serverLogger.info("Player '{}' {} ongoing game '{}', marked as disconnected", nickname, logAction, gameCode);
    }
  }

  /**
   * Deletes a game from the active games list and cleans up its related disconnected players
   *
   * @param gameCode  The game code to delete
   * @param gameEntry The game entry to delete
   */
  private void deleteGame(GameCode gameCode, GameEntry gameEntry) {
    StringBuilder message = new StringBuilder("Game '" + gameCode + "' has been deleted");

    ArrayList<String> cleanedUpPlayers = new ArrayList<>(4);
    // Clean up the game code in the attachment of connected players
    for (Client client : gameEntry.getClients()) {
      ClientAttachment attachment = (ClientAttachment) client.attachment();
      attachment.setGameCode(null);
      cleanedUpPlayers.add(attachment.nickname());
    }

    // Log cleaned up connected players
    if (!cleanedUpPlayers.isEmpty()) {
      message.append(", connected players cleaned up: ");
      cleanedUpPlayers.forEach(player -> message.append(player).append(", "));
      message.setLength(message.length() - 2);
    } else {
      message.append(", no connected players cleaned up");
    }

    cleanedUpPlayers.clear();
    // Clean up disconnected players
    GameInfo gameInfo = gameEntry.getGameInfo();
    for (String player : gameInfo.getJoinedPlayers()) {
      GameCode removedPlayer = disconnectedPlayers.remove(player);
      if (removedPlayer != null)
        cleanedUpPlayers.add(player);
    }

    // Log cleaned up disconnected players
    if (!cleanedUpPlayers.isEmpty()) {
      message.append(", disconnected players cleaned up: ");
      cleanedUpPlayers.forEach(player -> message.append(player).append(", "));
      message.setLength(message.length() - 2);
    } else {
      message.append(", no disconnected players cleaned up");
    }

    activeGames.remove(gameCode);
    serverLogger.info(message.toString());
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
    var heartbeatSchedule = scheduledExecutorService.schedule(new HeartbeatRunnable(client), HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
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
   * The heartbeat runnable that will be scheduled at fixed intervals for every client once it joins a lobby.
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
            var heartbeatSchedule = scheduledExecutorService.schedule(this, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
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
      handleClientRemoval(nickname, gameCode, true);
      // Always remove this player's nickname from the active nicknames list
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
