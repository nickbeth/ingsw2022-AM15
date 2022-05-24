package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.eriantys.server.Util.generateGameCode;

public class GameServer implements Runnable {
  private static final int HEARTBEAT_INTERVAL_SECONDS = 2;
  private static final int HEARTBEAT_DISCONNECTION_THRESHOLD = 5;

  private final BlockingQueue<MessageQueueEntry> messageQueue;
  private final HashMap<String, GameEntry> activeGames;
  private final Set<String> activeNicknames;
  private final ScheduledExecutorService heartbeatService;

  public GameServer(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
    this.activeGames = new HashMap<>();
    this.activeNicknames = new HashSet<>();
    this.heartbeatService = Executors.newScheduledThreadPool(1);
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

    switch (message.type()) {
      case PONG -> handlePong(client, message);

      case NICKNAME_REQUEST -> handleNicknameRequest(client, message);

      case CREATE_GAME -> handleCreateGame(client, message);
      case JOIN_GAME -> handleJoinGame(client, message);
      case SELECT_TOWER -> handleSelectTower(client, message);

      case START_GAME -> handleStartGame(client, message);

      case PLAY_ACTION -> handlePlayAction(client, message);

      case INTERNAL_SOCKET_ERROR -> handleSocketError(client, message);
    }
  }

  private void handlePong(Client client, Message message) {
    var attachment = (ClientAttachment) client.attachment();
    attachment.resetMissedHeartbeatCount();
  }

  private void handleNicknameRequest(Client client, Message message) {
    String nickname = message.nickname();
    if (nickname == null || nickname.isBlank()) {
      String errorMessage = String.format("Nickname '%s' is invalid", nickname);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // Check if a player with this nickname already exists, and add it if it doesn't
    if (!activeNicknames.add(nickname)) {
      String errorMessage = String.format("Nickname '%s' is already in use", nickname);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    client.attach(new ClientAttachment(nickname));
    client.send(new Message.Builder().type(MessageType.NICKNAME_OK).nickname(nickname).build());
  }

  private void handleCreateGame(Client client, Message message) {
    String gameCode = generateGameCode();
    GameEntry gameEntry = new GameEntry(message.gameInfo());
    gameEntry.addPlayer(message.nickname(), client);

    activeGames.put(gameCode, gameEntry);
    activeNicknames.add(message.nickname());
    ((ClientAttachment) client.attachment()).setGameCode(gameCode);

    client.send(new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
    initHeartbeat(client);
  }

  private void handleJoinGame(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    if (gameEntry == null) {
      String errorMessage = String.format("Game with code '%s' does not exist", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    // TODO: handle player reconnections
    if (gameEntry.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING) {
      String errorMessage = String.format("Game with code '%s' has already started", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (gameEntry.isFull()) {
      String errorMessage = String.format("Game with code '%s' is full", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.addPlayer(message.nickname(), client);
    ((ClientAttachment) client.attachment()).setGameCode(gameCode);

    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
    initHeartbeat(client);
  }

  private void handleSelectTower(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);

    TowerColor chosenTowerColor = message.gameInfo().getPlayerColor(message.nickname());
    if (gameEntry.getGameInfo().isTowerColorValid(message.nickname(), chosenTowerColor)) {
      String errorMessage = String.format("Tower color '%s' is not available", chosenTowerColor);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    gameEntry.setPlayerColor(message.nickname(), chosenTowerColor);
    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEINFO).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).build());
  }

  private void handleStartGame(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    GameAction action = message.gameAction();


    if (message.gameAction() == null) {
      String errorMessage = String.format("Game with code '%s' received a malformed initialization action", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    if (!gameEntry.executeAction(action)) {
      String errorMessage = String.format("Game with code '%s' tried to apply an invalid action: %s", gameCode, action.getClass().getSimpleName());
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }
    // Check if the game is ready and set the game as started
    if (!gameEntry.getGameInfo().start()) {
      String errorMessage = String.format("Game with code '%s' is not ready to be started", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.START_GAME).gameCode(gameCode).gameInfo(gameEntry.getGameInfo()).action(action).build());
  }

  private void handlePlayAction(Client client, Message message) {
    String gameCode = message.gameCode();
    GameEntry gameEntry = activeGames.get(gameCode);
    GameAction action = message.gameAction();

    if (!Objects.equals(message.nickname(), gameEntry.getCurrentPlayer())) {
      String errorMessage = String.format("Game with code '%s' received an action from an invalid player '%s'", gameCode, message.nickname());
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (message.gameAction() == null) {
      String errorMessage = String.format("Game with code '%s' received a malformed action", gameCode);
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    if (!gameEntry.executeAction(action)) {
      String errorMessage = String.format("Game with code '%s' tried to apply an invalid action: %s", gameCode, action.getClass().getSimpleName());
      client.send(new Message.Builder().type(MessageType.ERROR).error(errorMessage).build());
      return;
    }

    broadcastMessage(gameEntry, new Message.Builder().type(MessageType.GAMEDATA).gameCode(gameCode).action(action).build());
  }

  public void handleSocketError(Client client, Message message) {
    // Check that this message was created internally and is not coming from the network
    if (!message.nickname().equals(Client.SOCKET_ERROR_HASH))
      return;

    activeNicknames.remove(((ClientAttachment) client.attachment()).nickname());
    client.close();
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

  private void initHeartbeat(Client client) {
    heartbeatService.schedule(new Runnable() {
      @Override
      public void run() {
        var attachment = (ClientAttachment) client.attachment();
        if (attachment.increaseMissedHeartbeatCount() > HEARTBEAT_DISCONNECTION_THRESHOLD) {
          Logger.debug("Player '{}' playing game '{}' marked as disconnected", attachment.nickname(), attachment.gameCode());
          GameEntry gameEntry = activeGames.get(attachment.gameCode());
          // If game entry is null, ignore
          if (gameEntry == null)
            return;

          gameEntry.disconnectPlayer(attachment.nickname());
          return;
        }

        // Send another ping and re-schedule only if threshold was not reached
        client.send(new Message.Builder().type(MessageType.PING).build());
        heartbeatService.schedule(this, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
      }
    }, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
  }
}
