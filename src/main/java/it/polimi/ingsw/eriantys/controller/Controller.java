package it.polimi.ingsw.eriantys.controller;


import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.actions.InitiateGameEntities;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.GameInfo;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Controller manages the creation and the sending of the action
 */
public class Controller {
  private Client client;
  private ObservableActionInvoker invoker;
  private GameInfo info;

  public Controller(Client client, GameInfo info) {
    this.client = client;
    this.info = info;
  }

  public void messageCreator() {

  }

  // EXECUTED ONLY BY THE HOST, MUST BE MANAGED BEFORE
  public GameState gameInitializer() throws IOException {

    // Send message for creating server game
    client.send(new Message.Builder(MessageType.START_GAME)
            .gameInfo(info).build());
    invoker = new ObservableActionInvoker(new GameState(info.getPlayersNickname().size(), info.getMode()));

    // Initialize the game entities
    // todo verificare se l'inizializzazione del game è stata fatta secondo le regole

    // Initiate character cards
    List<CharacterCardEnum> characterCardEnums = new ArrayList<>(Arrays.asList(CharacterCardEnum.values()));

    // Initiate students on island
    StudentBag bag = new StudentBag().initStudents(RuleBook.STUDENT_PER_COLOR_SETUP);
    List<Students> studentsOnIslands = new ArrayList<>();
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      studentsOnIslands.add(new Students());
      studentsOnIslands.get(i).addStudent(bag.takeRandomStudent());
    }

    // Initiate entrances.
    bag = bag.initStudents(RuleBook.STUDENT_PER_COLOR - RuleBook.STUDENT_PER_COLOR_SETUP);
    List<Students> entrances = new ArrayList<>();
    for (int i = 0; i < info.getPlayersNickname().size(); i++) {
      entrances.add(new Students());
      for (int j = 0; j < info.getRules().entranceSize; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate entrances.
    List<Students> cloudsStudents = new ArrayList<>();
    for (int i = 0; i < info.getPlayersNickname().size(); i++) {
      cloudsStudents.add(new Students());
      for (int j = 0; j < info.getRules().playableStudentCount; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }
    GameAction action =
            new InitiateGameEntities(entrances, studentsOnIslands, cloudsStudents, characterCardEnums);

    client.send(new Message.Builder()
            .action(action)
            .gameInfo(info)
            .type(MessageType.GAMEDATA).build());
    invoker.executeAction(action);

    return null;
  }
}
