package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameStateTest {
  GameState game;
  GameState threePlayerGame;

  @BeforeEach
  public void setUp() {
    game = new GameState(2, GameMode.NORMAL);
    threePlayerGame = new GameState(3, GameMode.NORMAL);
    game.addPlayer("franco", TowerColor.WHITE);
    threePlayerGame.addPlayer("franco", TowerColor.WHITE);
    threePlayerGame.addPlayer("sberg", TowerColor.GRAY);
  }

  @Test
  void addPlayer() {
    game.addPlayer("gino", TowerColor.BLACK);
    assertTrue(game.getPlayers().stream()
            .anyMatch(p -> p.getNickname().equals("gino") && p.getColorTeam() == TowerColor.BLACK));
    assertTrue(game.getPlanOrderPlayers().stream()
            .anyMatch(p -> p.getNickname().equals("gino") && p.getColorTeam() == TowerColor.BLACK));
  }

  @Test
  void advancePlayer() {
    game.addPlayer("gino", TowerColor.BLACK);
    GameState spyedField = spy(game);

    //tests correct advancement for plan order
    doReturn(GamePhase.PLANNING).when(spyedField).getGamePhase();
    Player oldPlayerTwo = spyedField.getPlanOrderPlayers().get(1);
    spyedField.advancePlayer();
    assertEquals(oldPlayerTwo, spyedField.getCurrentPlayer());

    //tests correct advancement for action phase order
    spyedField.advancePlayer();
    spyedField.advanceGamePhase();

    doReturn(GamePhase.ACTION).when(spyedField).getGamePhase();
    oldPlayerTwo = spyedField.getTurnOrderPlayers().get(1);
    spyedField.advancePlayer();
    assertEquals(oldPlayerTwo, spyedField.getCurrentPlayer());
  }


  @Test
  void advanceGamePhaseToAction() {
    game.addPlayer("gino", TowerColor.BLACK);
    game.getPlayers().get(0).setPlayedCard(4);
    game.getPlayers().get(1).setPlayedCard(9);
    Player oldSecond = game.getPlayers().get(1);
    Player oldFirst = game.getPlayers().get(0);
    game.advanceGamePhase();

    assertEquals(oldSecond, game.getTurnOrderPlayers().get(0));
    assertEquals(oldFirst, game.getTurnOrderPlayers().get(1));
  }

  @Test
  void advanceGamePhaseToPlanning() {
    threePlayerGame.addPlayer("gino", TowerColor.BLACK);
    threePlayerGame.getPlayers().get(0).setPlayedCard(4);
    threePlayerGame.getPlayers().get(1).setPlayedCard(9);
    threePlayerGame.getPlayers().get(2).setPlayedCard(2);
    Player oldFirst = threePlayerGame.getPlayers().get(0);
    Player oldSecond = threePlayerGame.getPlayers().get(1);
    Player oldThird = threePlayerGame.getPlayers().get(2);
    threePlayerGame.advanceGamePhase();

    assertEquals(oldSecond, threePlayerGame.getTurnOrderPlayers().get(0));
    assertEquals(oldFirst, threePlayerGame.getTurnOrderPlayers().get(1));
    assertEquals(oldThird, threePlayerGame.getTurnOrderPlayers().get(2));

    threePlayerGame.advanceGamePhase();
    assertEquals(oldSecond, threePlayerGame.getPlanOrderPlayers().get(0));
    assertEquals(oldThird, threePlayerGame.getPlanOrderPlayers().get(1));
    assertEquals(oldFirst, threePlayerGame.getPlanOrderPlayers().get(2));
  }

  @Test
  void checkNoTowerWin() {
    game.addPlayer("gino", TowerColor.BLACK);
    List<Player> players = new ArrayList<>();
    GameState spyedGame = spy(game);
    Player p1 = spy(spyedGame.getPlayers().get(0));
    Player p2 = spy(spyedGame.getPlayers().get(1));
    Dashboard d1 = mock(Dashboard.class);
    Dashboard d2 = mock(Dashboard.class);

    // necessary stubbing
    doReturn(d1).when(p1).getDashboard();
    doReturn(d2).when(p2).getDashboard();
    when(d1.noMoreTowers()).thenReturn(true);
    when(d2.noMoreTowers()).thenReturn(true);
    players.add(p1);
    players.add(p2);
    doReturn(players).when(spyedGame).getPlayers();

    assertTrue(spyedGame.checkWinCondition());

    when(d2.noMoreTowers()).thenReturn(false);

    assertTrue(spyedGame.checkWinCondition());

    when(d1.noMoreTowers()).thenReturn(false);

    assertFalse(spyedGame.checkWinCondition());
  }

  @Test
  void checkNoCardsWin() {
    game.addPlayer("GINO", TowerColor.BLACK);
    List<Player> players = new ArrayList<>();
    GameState spyedGame = spy(game);
    Player p1 = spy(spyedGame.getPlayers().get(0));
    Player p2 = spy(spyedGame.getPlayers().get(1));

    ArrayList<AssistantCard> cards = new ArrayList<>();
    // necessary stubbing

    players.add(p1);
    players.add(p2);
    doReturn(players).when(spyedGame).getPlayers();
    assertFalse(spyedGame.checkWinCondition());

    doReturn(cards).when(p1).getCards();
    doReturn(Optional.empty()).when(p1).getChosenCard();
    assertTrue(spyedGame.checkWinCondition());

    Optional<AssistantCard> assistantCard = Optional.of(AssistantCard.FIVE);
    doReturn(assistantCard).when(p1).getChosenCard();
    assertFalse(spyedGame.checkWinCondition());
  }

  @Test
  void checkNoIslandsWin() {
    game.addPlayer("GINO", TowerColor.BLACK);

    GameState spyedGame = spy(game);
    PlayingField spyedField = spy(spyedGame.getPlayingField());

    when(spyedField.getIslandsAmount()).thenReturn(3);
    doReturn(spyedField).when(spyedGame).getPlayingField();

    assertTrue(spyedGame.checkWinCondition());

    when(spyedField.getIslandsAmount()).thenReturn(8);
    assertFalse(spyedGame.checkWinCondition());

  }


  @Test
  void getTowerWinner() {
    game.addPlayer("GINO", TowerColor.BLACK);
    List<Player> players = new ArrayList<>();
    GameState spyedGame = spy(game);
    Player p1 = spy(spyedGame.getPlayers().get(0));
    Player p2 = spy(spyedGame.getPlayers().get(1));
    players.add(p1);
    players.add(p2);


    p1.getDashboard().getTowers().count = 3;
    p2.getDashboard().getTowers().count = 2;

    doReturn(players).when(spyedGame).getPlayers();

    assertEquals(TowerColor.BLACK, spyedGame.getWinner().get());

  }

  @Test
  void getSameTowerWinner() {
    game.addPlayer("GINO", TowerColor.BLACK);
    List<Player> players = new ArrayList<>();
    GameState spyedGame = spy(game);
    Player p1 = spy(spyedGame.getPlayers().get(0));
    Player p2 = spy(spyedGame.getPlayers().get(1));
    players.add(p1);
    players.add(p2);
    PlayingField field = mock(PlayingField.class);

    p1.getDashboard().getTowers().count = 2;
    p2.getDashboard().getTowers().count = 2;


    doReturn(players).when(spyedGame).getPlayers();
    doReturn(field).when(spyedGame).getPlayingField();
    when(field.getHeldProfessorCount(TowerColor.WHITE)).thenReturn(2);
    when(field.getHeldProfessorCount(TowerColor.BLACK)).thenReturn(1);


    assertEquals(TowerColor.WHITE, spyedGame.getWinner().get());
  }

  @Test
  void noWinner() {
    game.addPlayer("GINO", TowerColor.BLACK);
    List<Player> players = new ArrayList<>();
    GameState spyedGame = spy(game);
    Player p1 = spy(spyedGame.getPlayers().get(0));
    Player p2 = spy(spyedGame.getPlayers().get(1));
    players.add(p1);
    players.add(p2);
    PlayingField field = mock(PlayingField.class);

    p1.getDashboard().getTowers().count = 2;
    p2.getDashboard().getTowers().count = 2;


    doReturn(players).when(spyedGame).getPlayers();
    doReturn(field).when(spyedGame).getPlayingField();
    when(field.getHeldProfessorCount(TowerColor.WHITE)).thenReturn(2);
    when(field.getHeldProfessorCount(TowerColor.BLACK)).thenReturn(2);

    assertEquals(Optional.empty(), spyedGame.getWinner());
  }

  @Test
  void advanceTurnPhase() {
    game.addPlayer("gino", TowerColor.BLACK);

    game.advanceTurnPhase();
    assertEquals(TurnPhase.MOVING, game.getTurnPhase());
    game.advanceTurnPhase();
    assertEquals(TurnPhase.PICKING, game.getTurnPhase());
    game.advanceTurnPhase();
    assertEquals(TurnPhase.PLACING, game.getTurnPhase());
    
  }
}