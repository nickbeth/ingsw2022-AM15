package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.actions.InitiateGameEntities;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewsTest {
  private static final Students students = new Students();
  private static final GameMode mode = GameMode.EXPERT;
  private static final int playerCount = 4;
  private static final RuleBook rules = RuleBook.makeRules(mode, playerCount);
  private static final GameState gameState = new GameState(playerCount, mode);

  @BeforeAll
  static void setUp() {
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
    for (int i = 0; i < playerCount; i++) {
      entrances.add(new Students());
      for (int j = 0; j < rules.entranceSize; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate clouds.
    List<Students> cloudsStudents = new ArrayList<>();
    for (int i = 0; i < rules.playableStudentCount; i++) {
      cloudsStudents.add(new Students());
      for (int j = 0; j < rules.playableStudentCount; j++) {
        cloudsStudents.get(i).addStudent(bag.takeRandomStudent());
      }
    }
    // Action Creation
    GameAction action = new InitiateGameEntities(entrances, studentsOnIslands, cloudsStudents, characterCardEnums);
//    action.apply(gameState);

    students.addStudents(HouseColor.PINK, 5);
    students.addStudents(HouseColor.RED, 1);
    students.addStudents(HouseColor.YELLOW, 8);
  }

  /**
   * Draw a dashboard, play around with it and draw it again.
   */
  @Test
  public void printDashboard() {
    RuleBook ruleBook = RuleBook.makeRules(GameMode.NORMAL, 3);
    Students entrance = new Students();
    entrance.addStudents(HouseColor.RED, 2);
    entrance.addStudents(HouseColor.BLUE, 3);
    entrance.addStudents(HouseColor.YELLOW, 1);
    entrance.addStudents(HouseColor.GREEN, 2);
    entrance.addStudents(HouseColor.PINK, 1);
    assertTrue(entrance.getCount() <= ruleBook.entranceSize);

    Dashboard dashboard = new Dashboard(entrance, ruleBook.dashboardTowerCount, TowerColor.WHITE);

    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    DashboardView dashboardView = new DashboardView(ruleBook, dashboard, professorHolder);
    System.out.println("Initial view:");
    dashboardView.draw(System.out);

    // Play around with the dashboard, then draw again
    dashboard.getDiningHall().addStudents(entrance);
    dashboard.getEntrance().tryRemoveStudents(entrance);
    // Only one of the following professors should be printed
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.BLUE);
    professorHolder.setProfessorHolder(TowerColor.BLACK, HouseColor.YELLOW);
    professorHolder.setProfessorHolder(TowerColor.GRAY, HouseColor.GREEN);
    dashboard.removeTowers(3);
    System.out.println("Dashboard view after some changes:");
    dashboardView.draw(System.out);
  }

  @Test
  public void printStudent() {
    View view = new StudentsView(students);

    view.draw(System.out);
  }

  @Test
  public void printIsland() {
    Island island = new Island(students);
    View view = new IslandView(island);

    view.draw(System.out);
  }

  @Test
  public void printIslands() {
    List<Island> islandList = new ArrayList<>();

    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.get(0).setLocked(true);
    islandList.get(2).setLocked(true);

    View view = new IslandsView(islandList, 0);

    view.draw(System.out);
  }

  @Test
  public void printAssistantCards() {
    List<AssistantCard> cards = Arrays.stream(AssistantCard.values()).toList();
    List<AssistantCard> subCards;

    System.out.println("- Test --------------------------------------------------");
    subCards = cards;
    new AssistantCardsView(subCards).draw(System.out);

    System.out.println("- Test --------------------------------------------------");
    subCards = cards.subList(0,5);
    new AssistantCardsView(subCards).draw(System.out);

    System.out.println("- Test --------------------------------------------------");
    subCards = cards.subList(0,8);
    new AssistantCardsView(subCards).draw(System.out);
  }

  @Test
  public void printClouds() {
    List<Cloud> clouds = new ArrayList<>();
    Cloud cloud = new Cloud(students);

    Students s = new Students();
    s.addStudent(HouseColor.PINK);
    s.addStudent(HouseColor.PINK);
    s.addStudent(HouseColor.PINK);
    Cloud cloudWith3Students = new Cloud(s);

    clouds.add(cloud);
    clouds.add(cloudWith3Students);

    new CloudsView(clouds).draw(System.out);

    clouds.add(cloud);
    clouds.add(cloud);

    new CloudsView(clouds).draw(System.out);

    clouds.add(cloudWith3Students);
    clouds.add(cloud);

    new CloudsView(clouds).draw(System.out);
  }

  @Test
  public void printGameLobby() {
    GameInfo lobby = new GameInfo(3, GameMode.EXPERT);
    lobby.addPlayer("gino", TowerColor.BLACK);
    lobby.addPlayer("minchia un nome lunghissimo", TowerColor.WHITE);
    lobby.addPlayer("franco");

    (new GameLobbyView(lobby, new GameCode("cia3"))).draw(System.out);
  }

  @Test
  public void printCharacterCards() {
    List<CharacterCard> cards = new ArrayList<>();
    cards.add(CharacterCardCreator.create(CharacterCardEnum.IGNORE_COLOR));
    cards.add(CharacterCardCreator.create(CharacterCardEnum.IGNORE_TOWERS));
    cards.add(CharacterCardCreator.create(CharacterCardEnum.STEAL_PROFESSOR));

    (new CharacterCardView(cards)).draw(System.out);
  }
}
