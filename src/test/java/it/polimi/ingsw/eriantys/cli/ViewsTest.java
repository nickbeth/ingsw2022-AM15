package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw.eriantys.cli.CustomPrintStream.out;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewsTest {
  private static final Students students = new Students();
  private static final GameMode mode = GameMode.EXPERT;
  private static final int playerCount = 4;
  private static final RuleBook rules = RuleBook.makeRules(mode, playerCount);

  @BeforeAll
  static void setUp() {
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
    Player player = new Player(rules, "Carnazza", TowerColor.WHITE, students);
    player.setDashboard(dashboard);


    DashboardView dashboardView = new DashboardView(player, ruleBook, professorHolder);
    out.println("Initial view:");
    dashboardView.draw(out);

    // Play around with the dashboard, then draw again
    dashboard.getDiningHall().addStudents(entrance);
    dashboard.getEntrance().tryRemoveStudents(entrance);
    // Only one of the following professors should be printed
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.BLUE);
    professorHolder.setProfessorHolder(TowerColor.BLACK, HouseColor.YELLOW);
    professorHolder.setProfessorHolder(TowerColor.GRAY, HouseColor.GREEN);
    dashboard.removeTowers(3);
    out.println("Dashboard view after some changes:");
    dashboardView.draw(out);
  }

  @Test
  public void printPlayers() {
    RuleBook rules = RuleBook.makeRules(GameMode.NORMAL, 2);
    Player p1 = new Player(rules, "Anna", TowerColor.WHITE, new Students());
    Player p2 = new Player(rules, "Bruno", TowerColor.WHITE, new Students());
    Player p3 = new Player(rules, "Carlo", TowerColor.WHITE, new Students());

    p2.setPlayedCard(0);
    p3.setPlayedCard(9);

    new PlayersView(List.of(p1, p2, p3), rules).draw(out);
  }

  @Test
  public void printDashboards() {
    RuleBook ruleBook = RuleBook.makeRules(GameMode.NORMAL, 3);

    Students entrance = new Students();
    entrance.addStudents(HouseColor.RED, 2);
    entrance.addStudents(HouseColor.BLUE, 3);
    entrance.addStudents(HouseColor.YELLOW, 1);
    entrance.addStudents(HouseColor.GREEN, 2);
    entrance.addStudents(HouseColor.PINK, 1);
    assertTrue(entrance.getCount() <= ruleBook.entranceSize);

    Player player = new Player(rules, "Carnazza", TowerColor.WHITE, students);
    Player player2 = new Player(rules, "Carnazza", TowerColor.WHITE, students);


    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));

    // Only one of the following professors should be printed
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.BLUE);
    professorHolder.setProfessorHolder(TowerColor.BLACK, HouseColor.YELLOW);
    professorHolder.setProfessorHolder(TowerColor.GRAY, HouseColor.GREEN);

    List<Player> players = new ArrayList<>(List.of(player, player2, player2, player, player));
    View view = new DashboardsView(players, ruleBook, professorHolder);

    for (int i = 0; i < players.size(); i++) {
      view.draw(out);
      players.remove(0);
    }
  }

  @Test
  public void printIsland() {
    Island island = new Island(students);
    View view = new IslandView(island);

    island.setLocked(true);
    island.setTowerCount(5);
    island.setTowerColor(TowerColor.WHITE);

    view.draw(out);

    island.setTowerCount(10);
    island.setTowerColor(TowerColor.GRAY);
    island.getStudents().addStudents(HouseColor.YELLOW, 5);

    view.draw(out);

    island.setTowerColor(TowerColor.BLACK);

    view.draw(out);

    island.setLocked(false);
    island.setTowerCount(5);
    island.setTowerColor(TowerColor.WHITE);

    view.draw(out);
  }

  @Test
  public void printIslands() {
    List<Island> islandList = new ArrayList<>();

    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));

    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));

    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));

    islandList.add(new Island(students));
    islandList.add(new Island(students));
    islandList.add(new Island(students));

    islandList.get(0).setLocked(true);
    islandList.get(2).setLocked(true);

    View view = new IslandsView(islandList, 0);

    for (int i = 0; i < 12; i++) {
      view.draw(out);
      islandList.remove(0);
    }
  }

  @Test
  public void printAssistantCards() {
    List<AssistantCard> cards = new ArrayList<>(Arrays.stream(AssistantCard.values()).toList());
    View view = new AssistantCardsView(cards);

    for (var ignored : AssistantCard.values()) {
      view.draw(out);
      cards.remove(0);
    }
    view.draw(out);
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

    new CloudsView(clouds).draw(out);

    clouds.add(cloud);
    clouds.add(cloud);

    new CloudsView(clouds).draw(out);

    clouds.add(cloudWith3Students);
    clouds.add(cloud);

    new CloudsView(clouds).draw(out);
  }

  @Test
  public void printGameLobby() {
    GameInfo lobby = new GameInfo(3, GameMode.EXPERT);
    lobby.addPlayer("gino", TowerColor.BLACK);
    lobby.addPlayer("tipo un nome lunghissimo", TowerColor.WHITE);
    lobby.addPlayer("franco");

    (new GameLobbyView(lobby, new GameCode("cia3"))).draw(out);
  }

  @Test
  public void printCharacterCards() {
    List<CharacterCard> cards = new ArrayList<>();

    for (var card : CharacterCardEnum.values()) {
      cards.add(CharacterCardCreator.create(card));
    }

    new CharacterCardsView(cards).draw(out);
  }

  @Test
  public void printGamesList() {
    List<String> players = new ArrayList<>();
    players.add("riso");
    players.add("patate");
    players.add("cozze");
    players.add("Marco");

    Map<GameCode, GameInfo> games = new HashMap<>();

    for (int i = 2; i <= 4; i++) {
      GameInfo tmp = new GameInfo(i, GameMode.NORMAL);
      int playerCount = i;
      players.forEach(nickname -> {
        if (!tmp.isFull())
          tmp.addPlayer(nickname);
        games.put(new GameCode("xxx" + playerCount), new GameInfo(tmp));
      });
    }

    new GamesListView(games).draw(out);
  }
}
