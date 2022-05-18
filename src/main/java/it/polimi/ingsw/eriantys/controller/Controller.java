package it.polimi.ingsw.eriantys.controller;


import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.network.MessageType.*;

/**
 * The Controller manages the creation and the sending of messages to the Server
 */
abstract public class Controller {
  protected final Client client;
  protected final ObservableActionInvoker invoker;
  protected final GameInfo gameInfo;
  protected final GameState gameState;
  protected final String playerNickname;

  public Controller(Client client, GameInfo gameInfo, String playerNickname) {
    this.client = client;
    this.gameInfo = gameInfo;
    this.gameState = new GameState(gameInfo.getMaxPlayerCount(), gameInfo.getMode());
    this.invoker = new ObservableActionInvoker(gameState);
    this.playerNickname = playerNickname;
  }

  // todo EXECUTED ONLY BY THE HOST, MUST BE MANAGED BEFORE

  /**
   * Creates the gameState in the client invoker. <br>
   * Send a START_GAME message to the server. <br>
   * Send the message with InitiateGameEntities action. <br>
   */
  public boolean startGame() {
    // Send message for creating server game
    // Initialize the game entities
    // todo verificare se l'inizializzazione del game è stata fatta secondo le regole

    if (!gameInfo.start())
      return false;
    // Initiate character cards
    List<CharacterCardEnum> characterCardEnums = new ArrayList<>(Arrays.asList(CharacterCardEnum.values()));

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
      for (int j = 0; j < gameState.getRuleBook().entranceSize; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate clouds.
    List<Students> cloudsStudents = new ArrayList<>();
    for (int i = 0; i < gameInfo.getMaxPlayerCount(); i++) {
      cloudsStudents.add(new Students());
      for (int j = 0; j < gameState.getRuleBook().playableStudentCount; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }
    // Action Creation
    GameAction action = new InitiateGameEntities(entrances, studentsOnIslands, cloudsStudents, characterCardEnums);

    client.send(new Message.Builder(START_GAME)
            .action(action)
            .gameInfo(gameInfo)
            .nickname(playerNickname)
            .build());

    return true;
  }

  /**
   * Send a message to the server with MoveMotherNature action
   */
  public boolean sendPickAssistantCard(int assistantCardIndex) {
    GameAction action = new PickAssistantCard(assistantCardIndex);

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(PLAY_ACTION)
            .action(action)
            .nickname(playerNickname)
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

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(PLAY_ACTION)
            .action(action)
            .nickname(playerNickname)
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

    client.send(new Message.Builder(GAMEDATA)
            .action(new RefillClouds(cloudsStudents))
            .gameInfo(gameInfo)
            .build());
  }

  /**
   * Send a message to the server with ActivateEffect action
   */
  public boolean sendActivateEffect(CharacterCard cc) {
    GameAction action = new ActivateCCEffect(cc);

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(GAMEDATA)
            .action(action)
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

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(PLAY_ACTION)
            .action(action)
            .nickname(playerNickname)
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

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(GAMEDATA)
            .action(action)
            .nickname(playerNickname)
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

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(PLAY_ACTION)
            .action(action)
            .nickname(playerNickname)
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

    if (!action.isValid(invoker.gameState))
      return false;

    client.send(new Message.Builder(PLAY_ACTION)
            .action(action)
            .nickname(playerNickname)
            .build());
    return true;
  }

  public void sendCreateGame(GameInfo gameInfo) {
    client.send(new Message.Builder(CREATE_GAME)
            .gameInfo(gameInfo)
            .nickname(playerNickname)
            .build());
  }

  public void sendJoinGame(GameInfo gameInfo) {
    client.send(new Message.Builder(JOIN_GAME)
            .gameInfo(gameInfo)
            .nickname(playerNickname)
            .build());
  }

  public boolean sendSelectTower(TowerColor color) {
    if (!gameInfo.isTowerColorValid(playerNickname, color))
      return false;
    gameInfo.addPlayer(playerNickname, color);
    client.send(new Message.Builder(SELECT_TOWER)
            .gameInfo(gameInfo)
            .nickname(playerNickname)
            .build());
    return true;
  }
  
  public GameState getGameState() {
    return gameState;
  }
}
