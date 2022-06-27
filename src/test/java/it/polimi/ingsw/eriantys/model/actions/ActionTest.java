package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.CustomPrintStream.out;
import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
  GameState normalGame;
  GameState expertGame;

  @BeforeEach
  void setUp() {
    normalGame = new GameState(3, GameMode.NORMAL);
    expertGame = new GameState(3, GameMode.EXPERT);
    normalGame.addPlayer("gino", TowerColor.WHITE);
    normalGame.addPlayer("franco", TowerColor.BLACK);
    normalGame.addPlayer("mauro", TowerColor.GRAY);
    expertGame.addPlayer("gino", TowerColor.WHITE);
    expertGame.addPlayer("franco", TowerColor.BLACK);
    expertGame.addPlayer("mauro", TowerColor.GRAY);
  }

  @Test
  void moveMotherNature() {
    GameAction action = new MoveMotherNature(3);
    normalGame.getPlayingField().moveMotherNature(11);
    normalGame.setTurnPhase(TurnPhase.MOVING);
    normalGame.setGamePhase(GamePhase.ACTION);
    normalGame.getCurrentPlayer().addToMaxMovement(3);

    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(2, normalGame.getPlayingField().getMotherNaturePosition());
    assertEquals(TurnPhase.PICKING, normalGame.getTurnPhase());
  }

  @Test
  void moveStudentsToDiningHall() {
    Students s = new Students();
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);

    Students entrance = new Students(s);
    entrance.addStudents(HouseColor.BLUE, normalGame.getRuleBook().entranceSize - 4);


    s.tryRemoveStudent(HouseColor.RED);

    normalGame.getCurrentPlayer().getDashboard().getEntrance().addStudents(entrance);

    GameAction action = new MoveStudentsToDiningHall(s);
    normalGame.setTurnPhase(TurnPhase.PLACING);
    normalGame.setGamePhase(GamePhase.ACTION);

    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(TurnPhase.PLACING, normalGame.getTurnPhase());
    //tests turn phase advancement when its last movement
    s.setStudents(new Students());
    s.addStudents(HouseColor.BLUE, 1);
    action = new MoveStudentsToDiningHall(s);
    action.apply(normalGame);
    assertEquals(TurnPhase.MOVING, normalGame.getTurnPhase());
    //check a false isValid
    s.setStudents(new Students());
    s.addStudents(HouseColor.RED, 2);
    action = new MoveStudentsToDiningHall(s);
    assertFalse(action.isValid(normalGame));
  }

  @Test
  void moveStudentsToIsland() {
    Students s = new Students();
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);
    s.addStudent(HouseColor.RED);

    Students islanders = new Students(s);
    islanders.addStudents(HouseColor.BLUE, normalGame.getRuleBook().entranceSize - 4);
    s.tryRemoveStudent(HouseColor.RED);

    normalGame.getCurrentPlayer().getDashboard().getEntrance().addStudents(islanders);

    GameAction action = new MoveStudentsToIsland(s, 0);
    normalGame.setTurnPhase(TurnPhase.PLACING);
    normalGame.setGamePhase(GamePhase.ACTION);

    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(TurnPhase.PLACING, normalGame.getTurnPhase());
    //tests turn phase advancement when its last movement
    s.setStudents(new Students());
    s.addStudents(HouseColor.BLUE, 1);
    action = new MoveStudentsToIsland(s, 0);
    action.apply(normalGame);
    assertEquals(TurnPhase.MOVING, normalGame.getTurnPhase());
    //check a false isValid
    s.setStudents(new Students());
    s.addStudents(HouseColor.RED, 2);
    action = new MoveStudentsToIsland(s, 0);
    assertFalse(action.isValid(normalGame));
  }

  @Test
  void initiateGameEntities() {
    List<Students> entrances = new ArrayList<>();
    List<Students> islands = new ArrayList<>();
    List<Students> clouds = new ArrayList<>();
    List<CharacterCardEnum> cards = new ArrayList<>();

    //setup entrance students
    Students s = new Students();
    s.addStudents(HouseColor.RED, 9);
    entrances.add(new Students(s));
    entrances.add(new Students(s));
    s.setStudents(new Students());
    s.addStudents(HouseColor.YELLOW, 9);
    entrances.add(new Students(s));

    //setup islands students
    s.setStudents(new Students());
    s.addStudent(HouseColor.BLUE);
    modelLogger.debug(String.valueOf(s.getCount()));
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      islands.add(new Students(s));
    }

    //setup clouds students
    s.setStudents(new Students());
    s.addStudents(HouseColor.GREEN, 4);
    for (int i = 0; i < expertGame.getRuleBook().cloudCount; i++) {
      clouds.add(new Students(s));
    }

    cards.add(CharacterCardEnum.ADD_TO_INFLUENCE);
    cards.add(CharacterCardEnum.ADD_TO_INFLUENCE);
    cards.add(CharacterCardEnum.ADD_TO_INFLUENCE);

    GameAction action = new InitiateGameEntities(entrances, islands, clouds, cards);

    assertTrue(action.isValid(expertGame));
    action.apply(expertGame);

    assertEquals(9, expertGame.getPlayers().get(0).getDashboard().getEntrance().getCount());
    assertEquals(9, expertGame.getPlayers().get(0).getDashboard().getEntrance().getCount(HouseColor.RED));
    assertEquals(9, expertGame.getPlayers().get(1).getDashboard().getEntrance().getCount());
    assertEquals(9, expertGame.getPlayers().get(1).getDashboard().getEntrance().getCount(HouseColor.RED));
    assertEquals(9, expertGame.getPlayers().get(2).getDashboard().getEntrance().getCount());
    assertEquals(9, expertGame.getPlayers().get(2).getDashboard().getEntrance().getCount(HouseColor.YELLOW));

    for (int i = 0; i < islands.size(); i++) {
      assertEquals(1, expertGame.getPlayingField().getIsland(i).getStudents().getCount());
      assertEquals(1, expertGame.getPlayingField().getIsland(i).getStudents().getCount(HouseColor.BLUE));
    }

    for (int i = 0; i < clouds.size(); i++) {
      assertEquals(4, expertGame.getPlayingField().getClouds().get(i).getStudents().getCount());
      assertEquals(4, expertGame.getPlayingField().getClouds().get(i).getStudents().getCount(HouseColor.GREEN));
    }

    for (int i = 0; i < cards.size(); i++) {
      assertEquals(cards.get(i), expertGame.getPlayingField().getCharacterCards().get(i).getCardEnum());
    }

  }

  @Test
  void chooseCC() {
    normalGame.getPlayingField().getCharacterCards().add(0, CharacterCardCreator.create(CharacterCardEnum.IGNORE_TOWERS));
    normalGame.setTurnPhase(TurnPhase.PLACING);
    normalGame.setGamePhase(GamePhase.ACTION);

    GameAction action = new ChooseCharacterCard(0);
    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(CharacterCardEnum.IGNORE_TOWERS, normalGame.getPlayingField().getPlayedCharacterCard().getCardEnum());
  }

  @Test
  void pickCloud() {
    int playersCount = 2;
    GameMode mode = GameMode.NORMAL;
    GameState game = new GameState(playersCount, mode);
    GameAction initiate = new InitiateGameEntities(new GameInfo(playersCount, mode));

    class Tmp {
      public static Students entrance(Player p) {
        return p.getDashboard().getEntrance();
      }
    }

    // Initiate game
    game.addPlayer("Paolo", TowerColor.WHITE);
    game.addPlayer("Alice", TowerColor.WHITE);
    initiate.apply(game);

    // Get players
    Player p1 = game.getPlayer("Paolo");
    Player p2 = game.getPlayer("Alice");

    // Empty players entrances
    Arrays.stream(HouseColor.values()).forEach(color -> {
      for (int i = 0; i < game.getRuleBook().entranceSize; i++) {
        Tmp.entrance(p1).tryRemoveStudent(color);
        Tmp.entrance(p2).tryRemoveStudent(color);
      }
    });
    int studentsLeftInEntrace = game.getRuleBook().entranceSize - game.getRuleBook().playableStudentCount;
    // this needs to pick a cloud
    p1.getDashboard().getEntrance().addStudents(HouseColor.PINK, studentsLeftInEntrace);
    // this needs to have fixed his dashboard
    p2.getDashboard().getEntrance().addStudents(HouseColor.PINK, studentsLeftInEntrace + 1);

    game.setCurrentPlayer(p1);
    Students oldCloudStudents = game.getPlayingField().getCloud(0).getStudents();
    new PickCloud(game, 0).apply(game);

    assertTrue(Tmp.entrance(p1).contains(oldCloudStudents));


    game.setCurrentPlayer(p2);
//    oldCloudStudents = game.getPlayingField().getCloud(1).getStudents();
    new PickCloud(game, 1).apply(game);

    assertEquals(game.getRuleBook().entranceSize, Tmp.entrance(p2).getCount());
  }

  @Test
  void refillClouds() {
    List<Students> clouds = new ArrayList<>();
    Students s = new Students();
    s.addStudents(HouseColor.GREEN, 4);
    for (int i = 0; i < normalGame.getRuleBook().cloudCount; i++) {
      clouds.add(new Students(s));
    }
    GameAction action = new RefillClouds(clouds);
    assertTrue(action.isValid(normalGame));
    clouds.add(new Students(s));
    assertFalse(action.isValid(normalGame));
  }

  @Test
  public void ActivateEffect() {
    GameState gameState = new GameState(4, GameMode.EXPERT);
    PlayingField field = gameState.getPlayingField();
    gameState.addPlayer("p1", TowerColor.WHITE);
    gameState.addPlayer("p2", TowerColor.BLACK);
    gameState.addPlayer("p3", TowerColor.WHITE);
    gameState.addPlayer("p4", TowerColor.BLACK);

    List<Students> entrances = new ArrayList<>();
    List<Students> islands = new ArrayList<>();
    List<Students> clouds = new ArrayList<>();
    List<CharacterCardEnum> cards = new ArrayList<>();

    //setup entrance students
    Students s = new Students();
    s.addStudents(HouseColor.RED, 9);
    entrances.add(new Students(s));
    entrances.add(new Students(s));
    s.setStudents(new Students());
    s.addStudents(HouseColor.YELLOW, 9);
    entrances.add(new Students(s));

    //setup islands students
    s.setStudents(new Students());
    s.addStudent(HouseColor.BLUE);
    modelLogger.debug(String.valueOf(s.getCount()));
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      islands.add(new Students(s));
    }
    islands.get(0).addStudents(HouseColor.RED, 5);


    //setup clouds students
    s.setStudents(new Students());
    s.addStudents(HouseColor.GREEN, 4);
    for (int i = 0; i < gameState.getRuleBook().cloudCount; i++) {
      clouds.add(new Students(s));
    }

    cards.add(CharacterCardEnum.IGNORE_COLOR);
    cards.add(CharacterCardEnum.ADD_TO_INFLUENCE);
    cards.add(CharacterCardEnum.LOCK_ISLAND);

    GameAction action = new InitiateGameEntities(entrances, islands, clouds, cards);

    action.apply(gameState);
    modelLogger.debug(String.valueOf(field.getCharacterCards().get(0)));

    gameState.getPlayingField().setPlayedCharacterCard(0);
    gameState.setTurnPhase(TurnPhase.EFFECT);
    for (int i = 0; i < 4; i++)
      gameState.advance();
    // Add coins to current player (p1)
    gameState.getCurrentPlayer().addCoins();
    gameState.getCurrentPlayer().addCoins();
    field.getIslands().forEach(is -> is.updateInfluences(field.getProfessorHolder()));
    field.getIsland(0).setTowerColor(TowerColor.WHITE);
    field.getIsland(0).setTowerCount(1);
    //gameState.getPlayingField().getIslands().forEach(island -> modelLogger.debug(island.getTeamsInfluenceTracer()));

    CharacterCard newCC = CharacterCardCreator.create(CharacterCardEnum.IGNORE_COLOR);
    int oldCardCost = field.getPlayedCharacterCard().getCost();
    ((ColorInputCards) newCC).setColor(HouseColor.RED);

    GameAction actionDue = new ActivateCCEffect(newCC);
    //(new IslandView(gameState.getPlayingField().getIsland(0))).draw(System.out);


    (new IslandsView(gameState.getPlayingField().getIslands(), 0)).draw(out);
    assertTrue(actionDue.isValid(gameState));
    actionDue.apply(gameState);
    modelLogger.debug(String.valueOf(field.getPlayedCharacterCard().getCardEnum()));
    assertEquals(RuleBook.INITIAL_COINS + 2, gameState.getCurrentPlayer().getCoins());
    assertEquals(RuleBook.INITIAL_COINS, field.getPlayedCharacterCard().getCost());
    // Add 4 coins to current player (p1): should result in 7 coins
    gameState.getCurrentPlayer().addCoins();
    gameState.getCurrentPlayer().addCoins();
    gameState.getCurrentPlayer().addCoins();
    gameState.getCurrentPlayer().addCoins();

    newCC = field.getCharacterCards().get(0);
    actionDue = new ActivateCCEffect(newCC);
    actionDue.apply(gameState);
    assertEquals(6, gameState.getCurrentPlayer().getCoins());
    assertEquals(oldCardCost + 1, field.getPlayedCharacterCard().getCost());
  }
}
