package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    normalGame.advanceGamePhase();
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
    normalGame.advanceGamePhase();

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
    normalGame.advanceGamePhase();

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
    Logger.debug(s.getCount());
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
    normalGame.advanceGamePhase();

    GameAction action = new ChooseCharacterCard(0);
    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(CharacterCardEnum.IGNORE_TOWERS, normalGame.getPlayingField().getPlayedCharacterCard().getCardEnum());
  }

  @Test
  void pickCloud() {

    List<Students> clouds = new ArrayList<>();
    Students s = new Students();
    s.addStudents(HouseColor.GREEN, 4);
    for (int i = 0; i < normalGame.getRuleBook().cloudCount; i++) {
      clouds.add(new Students(s));
    }
    List<Students> entrances = new ArrayList<>();
    //initiate game entities
    s.setStudents(new Students());
    s.addStudents(HouseColor.RED, 9);
    entrances.add(new Students(s));
    s.setStudents(new Students());
    s.addStudents(HouseColor.YELLOW, 5);
    entrances.add(new Students(s));
    entrances.add(new Students(s));
    GameAction action = new InitiateGameEntities(entrances, new ArrayList<>(), clouds, new ArrayList<>());

    //second to last player
    action.apply(normalGame);
    action = new PickCloud(0);
    normalGame.advanceGamePhase();
    normalGame.setTurnPhase(TurnPhase.PICKING);
    normalGame.advancePlayer();
    assertTrue(action.isValid(normalGame));
    action.apply(normalGame);
    assertEquals(TurnPhase.PLACING, normalGame.getTurnPhase());
    assertEquals(GamePhase.ACTION, normalGame.getGamePhase());
    assertEquals(normalGame.getTurnOrderPlayers().get(2), normalGame.getCurrentPlayer());

    //last player
    action = new PickCloud(1);
    normalGame.setTurnPhase(TurnPhase.PICKING);
    assertTrue(action.isValid(normalGame));

    action.apply(normalGame);
    assertEquals(TurnPhase.PLACING, normalGame.getTurnPhase());
    assertEquals(GamePhase.PLANNING, normalGame.getGamePhase());
    assertEquals(normalGame.getPlanOrderPlayers().get(0), normalGame.getCurrentPlayer());

    //is valid tests
    action = new PickCloud(6);
    assertFalse(action.isValid(normalGame));

    action = new PickCloud(0);
    assertFalse(action.isValid(normalGame));
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
    GameState gameState = spy(new GameState(4, GameMode.NORMAL));
    PlayingField field = spy(new PlayingField(RuleBook.makeRules(GameMode.NORMAL, 4)));
    gameState.addPlayer("p1", TowerColor.WHITE);
    gameState.addPlayer("p2", TowerColor.BLACK);
    gameState.addPlayer("p3", TowerColor.WHITE);
    gameState.addPlayer("p4", TowerColor.BLACK);

    doReturn(field).when(gameState).getPlayingField();
    CharacterCard cc = CharacterCardCreator.create(CharacterCardEnum.IGNORE_COLOR);

    CharacterCard sCC = spy(cc);
    doNothing().when(sCC).applyEffect(any());
    doReturn(sCC).when(field).getPlayedCharacterCard();

    gameState.getPlayingField().getCharacterCards()
            .add(cc);
    gameState.getPlayingField().setPlayedCharacterCard(0);
    gameState.setTurnPhase(TurnPhase.EFFECT);
    gameState.advanceGamePhase();
    gameState.getCurrentPlayer().addCoin();
    gameState.getCurrentPlayer().addCoin();
    CharacterCard newCC = CharacterCardCreator.create(CharacterCardEnum.IGNORE_COLOR);
    ((ColorInputCards) newCC).setColor(HouseColor.RED);
    GameAction action = new ActivateCCEffect(newCC);


    assertTrue(action.isValid(gameState));
    action.apply(gameState);
    assertEquals(newCC, gameState.getPlayingField().getCharacterCards().get(0));
    assertEquals(sCC, gameState.getPlayingField().getPlayedCharacterCard());
  }


}
