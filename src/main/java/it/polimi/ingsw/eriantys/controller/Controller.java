package it.polimi.ingsw.eriantys.controller;


import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.*;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.GameInfo;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.network.MessageType.GAMEDATA;
import static it.polimi.ingsw.eriantys.network.MessageType.START_GAME;

/**
 * The Controller manages the creation and the sending of messages to the Server
 */
abstract public class Controller {
  private Client client;
  private ObservableActionInvoker invoker;
  private GameInfo info;

  public Controller(Client client, GameInfo info) {
    this.client = client;
    this.info = info;
  }

  // todo EXECUTED ONLY BY THE HOST, MUST BE MANAGED BEFORE

  /**
   * Creates the gameState in the client invoker. <br>
   * Send a START_GAME message to the server. <br>
   * Send the message with InitiateGameEntities action. <br>
   */
  public void gameInitializer() {

    // Send message for creating server game
    client.send(new Message.Builder(START_GAME)
        .gameInfo(info).build());
    invoker = new ObservableActionInvoker(new GameState(info.getPlayersNickname().size(), info.getMode()));

    // Initialize the game entities
    // todo verificare se l'inizializzazione del game è stata fatta secondo le regole

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
    for (int i = 0; i < info.getPlayersNickname().size(); i++) {
      entrances.add(new Students());
      for (int j = 0; j < info.getRules().entranceSize; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate clouds.
    List<Students> cloudsStudents = new ArrayList<>();
    for (int i = 0; i < info.getPlayersNickname().size(); i++) {
      cloudsStudents.add(new Students());
      for (int j = 0; j < info.getRules().playableStudentCount; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }
    // Action Creation
    GameAction action =
        new InitiateGameEntities(entrances, studentsOnIslands, cloudsStudents, characterCardEnums);

    client.send(new Message.Builder()
        .action(action)
        .gameInfo(info)
        .type(MessageType.INITIALIZE_GAME).build());
    //  invoker.executeAction(action); // once server returns the same action it will be invoked
  }

  /**
   * Send a message to the server with MoveMotherNature action
   */
  public void sendPickAssistantCard(int assistantCardIndex) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new PickAssistantCard(assistantCardIndex))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with sendPickCloud action
   *
   * @param cloudIndex Index of the position of the chosen island
   */
  public void sendPickCloud(int cloudIndex) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new PickCloud(cloudIndex))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with RefillCloud action
   */
  public void sendRefillCloud() {
    List<Students> cloudsStudents = new ArrayList<>();
    Students temp = new Students();
    StudentBag currentBag = invoker.getGameState().getPlayingField().getStudentBag();

    // Populate clouds with random students from bag
    for (int cloudIter = 0; cloudIter < info.getRules().cloudCount; cloudIter++) {
      for (int cloudSizeIter = 0; cloudSizeIter < info.getRules().playableStudentCount; cloudSizeIter++) {
        temp.addStudent(currentBag.takeRandomStudent());
      }
      cloudsStudents.add(new Students(temp));
      temp = new Students(); // clear temp
    }

    client.send(new Message.Builder(GAMEDATA)
        .action(new RefillClouds(cloudsStudents))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with ActivateEffect action
   */
  public void sendActivateEffect(CharacterCard cc) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new ActivateCCEffect(cc))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with ChooseCharacterCard action
   *
   * @param ccIndex Index of the position of the chosen character card
   */
  public void sendChooseCharacterCard(int ccIndex) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new ChooseCharacterCard(ccIndex))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with MoveMotherNature action
   *
   * @param amount Amount of island mother nature is wanted to be moved
   */
  public void sendMoveMotherNature(int amount) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new MoveMotherNature(amount))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with MoveStudentsToDiningHall action
   *
   * @param students Students to move
   */
  public void sendMoveStudentsToDiningHall(Students students) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new MoveStudentsToDiningHall(students))
        .gameInfo(info)
        .build());
  }

  /**
   * Send a message to the server with MoveStudentsToIsland action
   *
   * @param students    Students to move
   * @param islandIndex Index of island destination
   */
  public void sendMoveStudentsToIsland(Students students, int islandIndex) {
    client.send(new Message.Builder(GAMEDATA)
        .action(new MoveStudentsToIsland(students, islandIndex))
        .gameInfo(info)
        .build());
  }
}
