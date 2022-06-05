package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.RuleBook.PLAYABLE_CC_AMOUNT;
import static it.polimi.ingsw.eriantys.network.MessageType.*;

/**
 * The Controller manages the creation and the sending of messages to the Server
 */
abstract public class Controller implements Runnable {
  private static Controller controller;

  public static Controller create(boolean isGui, Client networkClient) {
    if (controller == null) {
      if (isGui) {
        controller = new GuiController(networkClient);
      } else {
        controller = new CliController(networkClient);
      }
    }
    return controller;
  }

  public static Controller getController() {
    return controller;
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
      return false;
    }
    return true;
  }

  public void disconnect() {
    networkClient.close();
  }

  public void initGame() {
    gameState = new GameState(gameInfo.getMaxPlayerCount(), gameInfo.getMode());

    for (var playerEntry : gameInfo.getPlayersMap().entrySet())
      gameState.addPlayer(playerEntry.getKey(), playerEntry.getValue());
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

  public void setPlayerConnected(boolean connected, String nickname) {
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
      if (!gameInfo.start())
        return false;

      RuleBook rules = RuleBook.makeRules(gameInfo.getMode(), gameInfo.getMaxPlayerCount());
      // Initiate character cards
      Random rand = new Random();
      List<CharacterCardEnum> chosenCharacterCards = new ArrayList<>();
      for (int i = 0; i < PLAYABLE_CC_AMOUNT; i++) {
        int randomCCIndex = rand.nextInt(0, CharacterCardEnum.values().length);
        chosenCharacterCards.add(CharacterCardEnum.values()[randomCCIndex]);
      }

      // Initiate students on island
      StudentBag bag = new StudentBag();
      bag.initStudents(RuleBook.STUDENT_PER_COLOR_SETUP);
      List<Students> studentsOnIslands = new ArrayList<>();
      for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
        studentsOnIslands.add(new Students());
        if (i != 0 && i != 6)
          studentsOnIslands.get(i).addStudent(bag.takeRandomStudent());
      }

      // Initiate entrances.
      bag.initStudents(RuleBook.STUDENT_PER_COLOR - RuleBook.STUDENT_PER_COLOR_SETUP);
      List<Students> entrances = new ArrayList<>();
      for (int i = 0; i < gameInfo.getMaxPlayerCount(); i++) {
        entrances.add(new Students());
        for (int j = 0; j < rules.entranceSize; j++) {
          entrances.get(i).addStudent(bag.takeRandomStudent());
        }
      }

      // Initiate clouds.
      List<Students> cloudsStudents = new ArrayList<>();
      for (int i = 0; i < gameInfo.getMaxPlayerCount(); i++) {
        cloudsStudents.add(new Students());
        for (int j = 0; j < rules.playableStudentCount; j++) {
          cloudsStudents.get(i).addStudent(bag.takeRandomStudent());
        }
      }
      // Action Creation
      GameAction action = new InitiateGameEntities(entrances, studentsOnIslands, cloudsStudents, chosenCharacterCards);

      Message msg = new Message.Builder(START_GAME)
          .action(action)
          .text(nickname + " is starting the game.")
          .gameCode(gameCode)
          .gameInfo(gameInfo)
          .nickname(nickname)
          .build();
      networkClient.send(msg);

      clientLogger.info("Sending message {} to the server", msg.getType());
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
          .text(nickname + " picked an assistant card")
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      return true;
    }

    /**
     * Send a message to the server with sendPickCloud action
     *
     * @param cloudIndex Index of the position of the chosen island
     */
    public boolean sendPickCloud(int cloudIndex) {
      GameAction action = new PickCloud(cloudIndex);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(PLAY_ACTION)
          .action(action)
          .text(MessageFormat.format("{0} picked {1} cloud", nickname, cloudIndex))
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      return true;
    }

    /**
     * Send a message to the server with RefillCloud action
     */
    public void sendRefillCloud() {
      List<Students> cloudsStudents = new ArrayList<>();
      Students temp = new Students();
      StudentBag currentBag = gameState.getPlayingField().getStudentBag();

      // Populate clouds with random students from bag
      for (int cloudIter = 0; cloudIter < gameState.getRuleBook().cloudCount; cloudIter++) {
        for (int cloudSizeIter = 0; cloudSizeIter < gameState.getRuleBook().playableStudentCount; cloudSizeIter++) {
          temp.addStudent(currentBag.takeRandomStudent());
        }
        cloudsStudents.add(new Students(temp));
        temp = new Students(); // clear temp
      }

      networkClient.send(new Message.Builder(GAMEDATA)
          .action(new RefillClouds(cloudsStudents))
          .gameCode(gameCode)
          .gameInfo(gameInfo)
          .build());
    }

    /**
     * Send a message to the server with ActivateEffect action
     */
    public boolean sendActivateEffect(CharacterCard cc) {
      GameAction action = new ActivateCCEffect(cc);

      if (!action.isValid(gameState))
        return false;

      networkClient.send(new Message.Builder(GAMEDATA)
          .action(action)
          .gameCode(gameCode)
          .gameInfo(gameInfo)
          .build());
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

      networkClient.send(new Message.Builder(GAMEDATA)
          .action(action)
          .text(String.format("%s moved mother nature by %d islands", nickname, amount))
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
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
          .text(String.format("%s moved his students to the dining hall", nickname))
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
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
          .text(String.format("%s moved his students to %d island", nickname, islandIndex))
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      return true;
    }

    public void sendNickname(String nickname) {
      networkClient.send(new Message.Builder(NICKNAME_REQUEST)
          .nickname(nickname)
          .build());
    }

    public void sendCreateGame(int numberOfPlayers, GameMode gameMode) {
      gameInfo = new GameInfo(numberOfPlayers, gameMode);
      networkClient.send(new Message.Builder(CREATE_GAME)
          .gameInfo(gameInfo)
          .nickname(nickname)
          .build());
    }


    public void sendJoinGame(GameCode gameCode) {
      networkClient.send(new Message.Builder(JOIN_GAME)
          .gameInfo(gameInfo)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
    }

    public boolean sendSelectTower(TowerColor color) {
      if (!gameInfo.isTowerColorValid(nickname, color))
        return false;
      gameInfo.addPlayer(nickname, color);
      networkClient.send(new Message.Builder(SELECT_TOWER)
          .gameInfo(gameInfo)
          .gameCode(gameCode)
          .nickname(nickname)
          .build());
      return true;
    }


  }

  public PropertyChangeSupport getListenerHolder() {
    return listenerHolder;
  }
}
