package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.network.MessageType.*;

/**
 * The Controller manages the creation and the sending of messages to the Server.
 * <br><br>
 * This class is a singleton, an instance may be obtained by calling the {@link #get()} method.
 */
abstract public class Controller implements Runnable {
  private static Controller instance;

  public static Controller create(boolean isGui, Client networkClient) {
    if (instance == null) {
      if (isGui) {
        instance = new GuiController(networkClient);
      } else {
        instance = new CliController(networkClient);
      }
    }
    return instance;
  }

  /**
   * Returns the global instance of the Controller.
   *
   * @return The controller instance
   */
  public static Controller get() {
    return instance;
  }

  protected GameInfo gameInfo;
  protected Sender sender;
  protected Client networkClient;
  protected GameState gameState;

  protected String nickname;
  protected GameCode gameCode;

  protected PropertyChangeSupport listenerHolder;

  public Controller(Client networkClient) {
    this.networkClient = networkClient;
    sender = new Sender();
    listenerHolder = new PropertyChangeSupport(this);
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public boolean connect(String address, int port) {
    try {
      networkClient.connect(address, port);
      // Launch the network listening thread
      Thread socketThread = new Thread(networkClient, "socket");
      socketThread.setDaemon(true);
      socketThread.start();
    } catch (IOException e) {
      clientLogger.error("An error occurred while connecting to the server: ", e);
      return false;
    }
    return true;
  }

  public void disconnect() {
    networkClient.close();
  }

  public void initGame() {
    initEmptyGame();

    for (var playerEntry : gameInfo.getPlayersMap().entrySet())
      gameState.addPlayer(playerEntry.getKey(), playerEntry.getValue());
  }

  public void initEmptyGame() {
    gameState = new GameState(gameInfo.getMaxPlayerCount(), gameInfo.getMode());
  }

  /**
   * Applies the given {@link GameAction} to the game state.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if action was valid and was applied successfully, {@code false} otherwise
   */
  public boolean executeAction(GameAction action) {
    synchronized (gameState) {
      if (action.isValid(gameState)) {
        action.apply(gameState);
        modelLogger.info("Executed action {}", action.getClass().getSimpleName());
        return true;
      }
    }
    return false;
  }

  abstract public void showError(String error);

  /**
   * base implementation does nothing
   */
  public void showNetworkError(String error) {
  }

  public void setPlayerConnection(boolean connected, String nickname) {
    gameState.getPlayer(nickname).setConnected(connected);
  }

  /**
   * Add a listener for events with the given tag.
   *
   * @param listener The listener to be subscribed
   * @param tag      The event tag
   */
  public void addListener(PropertyChangeListener listener, String tag) {
    listenerHolder.addPropertyChangeListener(tag, listener);
  }

  public abstract void fireChange(EventType event, Object oldValue, Object newValue);

  /**
   * Removes the given listener.
   *
   * @param listener Listener to be unsubscribed
   */
  public void removeListener(PropertyChangeListener listener) {
    listenerHolder.removePropertyChangeListener(listener);
  }

  /**
   * Removes the given listener for events with the given tag.
   *
   * @param listener The listener to be unsubscribed
   * @param tag      The event tag
   */
  public void removeListener(PropertyChangeListener listener, String tag) {
    listenerHolder.removePropertyChangeListener(tag, listener);
  }

  public GameInfo getGameInfo() {
    return gameInfo;
  }

  public void setGameInfo(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
  }

  public GameState getGameState() {
    return gameState;
  }

  public String getNickname() {
    return nickname;
  }

  public GameCode getGameCode() {
    return gameCode;
  }

  public void setGameCode(GameCode gameCode) {
    this.gameCode = gameCode;
  }

  public Sender sender() {
    return sender;
  }

  public class Sender {

    /**
     * Sends a START_GAME message to the server, containing the initiateGameEntities action. <br>
     */
    public boolean sendStartGame() {
      if (!gameInfo.isReady())
        return false;

      GameAction action = new InitiateGameEntities(gameInfo);

      Message msg = new Message.Builder(START_GAME)
          .action(action)
          .gameCode(gameCode)
          .gameInfo(gameInfo)
          .nickname(nickname)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sent message {} to the server", msg.type());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());
      return true;
    }

    /**
     * Send a message to the server with MoveMotherNature action
     */
    public boolean sendPickAssistantCard(int assistantCardIndex) {
      GameAction action = new PickAssistantCard(assistantCardIndex);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with sendPickCloud action
     *
     * @param cloudIndex Index of the position of the chosen island
     */
    public boolean sendPickCloud(int cloudIndex) {
      GameAction action = new PickCloud(gameState, cloudIndex);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with RefillCloud action
     */
    public void sendRefillClouds() {
      GameAction action = new RefillClouds(gameState);
      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .nickname(gameState.getCurrentPlayer().getNickname())
          .gameCode(gameCode)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());
    }


    /**
     * Send a message to the server with ActivateEffect action
     */
    public boolean sendActivateEffect(CharacterCard cc) {
      GameAction action = new ActivateCCEffect(cc);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .nickname(nickname)
          .gameCode(gameCode)
          .gameInfo(gameInfo)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with ChooseCharacterCard action
     *
     * @param ccIndex Index of the position of the chosen character card
     */
    public boolean sendChooseCharacterCard(int ccIndex) {
      GameAction action = new ChooseCharacterCard(ccIndex);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with MoveMotherNature action
     *
     * @param amount Amount of island mother nature is wanted to be moved
     */
    public boolean sendMoveMotherNature(int amount) {
      GameAction action = new MoveMotherNature(amount);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {}", action.getClass().getSimpleName());

      return true;
    }

    public boolean sendAdvanceState() {
      GameAction action = new AdvanceState();

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with MoveStudentsToDiningHall action
     *
     * @param students Students to move
     */
    public boolean sendMoveStudentsToDiningHall(Students students) {
      GameAction action = new MoveStudentsToDiningHall(students);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    /**
     * Send a message to the server with MoveStudentsToIsland action
     *
     * @param students    Students to move
     * @param islandIndex Index of island destination
     * @return True if the action is valid and sent.
     */
    public boolean sendMoveStudentsToIsland(Students students, int islandIndex) {
      GameAction action = new MoveStudentsToIsland(students, islandIndex);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      clientLogger.info("Sent action {} to the server", action.getClass().getSimpleName());

      return true;
    }

    public void sendNickname(String nickname) {
      Message msg = new Message.Builder(NICKNAME_REQUEST)
          .nickname(nickname)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sent message {} to the server", msg);
    }

    public void sendCreateGame(int numberOfPlayers, GameMode gameMode) {
      gameInfo = new GameInfo(numberOfPlayers, gameMode);
      Message msg = new Message.Builder(CREATE_GAME)
          .gameInfo(gameInfo)
          .nickname(nickname)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sent message {} to the server", msg);
    }


    public void sendJoinGame(GameCode gameCode) {
      Message msg = new Message.Builder(JOIN_GAME)
          .gameInfo(gameInfo)
          .gameCode(gameCode)
          .nickname(nickname)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sent message {} to the server", msg);
    }

    public void sendQuitGame() {
      Message msg = new Message.Builder(QUIT_GAME)
          .nickname(nickname)
          .gameCode(gameCode)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sent message {} to the server", msg);
    }

    public boolean sendSelectTower(TowerColor color) {
      if (!gameInfo.isTowerColorValid(nickname, color))
        return false;
      gameInfo.addPlayer(nickname, color);

      Message msg = new Message.Builder(SELECT_TOWER)
          .gameInfo(gameInfo)
          .gameCode(gameCode)
          .nickname(nickname)
          .build();
      networkClient.send(msg);
      return true;
    }
  }

  public PropertyChangeSupport getListenerHolder() {
    return listenerHolder;
  }
}
