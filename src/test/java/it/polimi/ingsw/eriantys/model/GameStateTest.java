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
    assertTrue(game.getPlanningPhaseOrder().stream()
        .anyMatch(p -> p.getNickname().equals("gino") && p.getColorTeam() == TowerColor.BLACK));
  }

  @Test
  void advance() {
    game.addPlayer("gino", TowerColor.BLACK);
    Player franco = game.getPlayer("franco");
    Player gino = game.getPlayer("gino");
    gino.setPlayedCard(0); // Play card (1, 1)
    franco.setPlayedCard(1); // Play card (2, 1)
    // Expected order:
    // Planning phase: franco -> gino
    // Action phase: gino -> franco

    assertEquals(game.getGamePhase(), GamePhase.PLANNING);
    assertEquals(game.getCurrentPlayer(), franco);

    game.advance(); // PLANNING - gino
    assertEquals(game.getCurrentPlayer(), gino);

    game.advance(); // ACTION - PLACING - gino
    assertEquals(game.getGamePhase(), GamePhase.ACTION);
    assertEquals(game.getTurnPhase(), TurnPhase.PLACING);
    assertEquals(game.getCurrentPlayer(), gino);

    game.advance(); // ACTION - MOVING - gino
    assertEquals(game.getGamePhase(), GamePhase.ACTION);
    assertEquals(game.getTurnPhase(), TurnPhase.MOVING);
    assertEquals(game.getCurrentPlayer(), gino);

    game.advance(); // ACTION - PICKING - gino
    assertEquals(game.getGamePhase(), GamePhase.ACTION);
    assertEquals(game.getTurnPhase(), TurnPhase.PICKING);
    assertEquals(game.getCurrentPlayer(), gino);

    game.advance(); // ACTION - PLACING - franco
    assertEquals(game.getGamePhase(), GamePhase.ACTION);
    assertEquals(game.getTurnPhase(), TurnPhase.PLACING);
    assertEquals(game.getCurrentPlayer(), franco);

    game.advance(); // ACTION - MOVING - franco
    game.advance(); // ACTION - PICKING - franco
    game.advance(); // PLANNING - gino
    assertEquals(game.getGamePhase(), GamePhase.PLANNING);
    assertEquals(game.getCurrentPlayer(), gino);

    // Disconnect gino, gino should be skipped and should now be turn of franco
    gino.setConnected(false);
    game.advance(); // PLANNING - franco
    assertEquals(game.getGamePhase(), GamePhase.PLANNING);
    assertEquals(game.getCurrentPlayer(), franco);

    gino.setConnected(true);
    game.advance(); // ACTION - PLACING - gino
    game.advance(); // ACTION - MOVING - gino
    game.advance(); // ACTION - PICKING - gino

    // Disconnect franco, it should be skipped entirely and go to planning
    // franco should also be skipped in the planning phase
    // Should result in gino playing in the planning phase
    franco.setConnected(false);
    game.advance(); // PLANNING - gino
    assertEquals(game.getGamePhase(), GamePhase.PLANNING);
    assertEquals(game.getCurrentPlayer(), gino);
  }

  @Test
  void advancePlayer() {
    game.addPlayer("gino", TowerColor.BLACK);
    GameState spyedField = spy(game);

    //tests correct advancement for plan order
    doReturn(GamePhase.PLANNING).when(spyedField).getGamePhase();
    Player oldPlayerTwo = spyedField.getPlanningPhaseOrder().get(1);
    spyedField.advance();
    assertEquals(oldPlayerTwo, spyedField.getCurrentPlayer());

    // Tests correct setup of action phase order
    spyedField.getPlayer("gino").setPlayedCard(5);
    spyedField.getPlayer("franco").setPlayedCard(1);
    spyedField.advance();
    assertEquals(List.of("franco", "gino"), spyedField.getActionPhaseOrder().stream().map(Player::getNickname).toList());

    // Advance to PLANNING phase
    // We need to advance 3 (turn phase count) times 2 (number of players): 6 times
    for (int i = 0; i < 6; i++)
      spyedField.advance();
    assertEquals(List.of("franco", "gino"), spyedField.getPlanningPhaseOrder().stream().map(Player::getNickname).toList());

    spyedField.getPlayer("gino").setPlayedCard(0);
    spyedField.getPlayer("franco").setPlayedCard(5);
    // Advance to ACTION phase
    spyedField.advance();
    spyedField.advance();
    assertEquals(List.of("gino", "franco"), spyedField.getActionPhaseOrder().stream().map(Player::getNickname).toList());

    // Advance to PLANNING phase
    // We need to advance 3 (turn phase count) times 2 (number of players): 6 times
    for (int i = 0; i < 6; i++)
      spyedField.advance();
    assertEquals(List.of("gino", "franco"), spyedField.getPlanningPhaseOrder().stream().map(Player::getNickname).toList());
  }

  @Test
  void advancePlayerWithDisconnections() {
    game.addPlayer("gino", TowerColor.BLACK);
    GameState spyedField = spy(game);

    game.getPlayer("gino").setConnected(false);
    //tests correct advancement for plan order while one player is disconnected
    doReturn(GamePhase.PLANNING).when(spyedField).getGamePhase();
    Player oldPlayerOne = spyedField.getPlanningPhaseOrder().get(0);
    spyedField.advance();
    assertEquals(oldPlayerOne, spyedField.getCurrentPlayer());
  }

  @Test
  void advanceGamePhaseToAction() {
    game.addPlayer("gino", TowerColor.BLACK);
    Player franco = game.getPlayers().get(0);
    Player gino = game.getPlayers().get(1);
    franco.setPlayedCard(4); // franco plays (4, 2)
    gino.setPlayedCard(9); // gino plays (9, 5)
    // Advance to ACTION phase
    game.advance();
    game.advance();

    // Expected order:
    // franco -> gino
    assertEquals(franco, game.getActionPhaseOrder().get(0));
    assertEquals(gino, game.getActionPhaseOrder().get(1));

    // Advance to PLANNING phase
    // We need to advance 3 (turn phase count) times 2 (number of players): 6 times
    for (int i = 0; i < 6; i++)
      game.advance();
    franco.setPlayedCard(8); // franco plays (8, 4)
    gino.setPlayedCard(2); // gino plays (2, 1)
    game.advance();
    game.advance();
    // Expected order:
    // gino -> franco
    assertEquals(gino, game.getActionPhaseOrder().get(0));
    assertEquals(franco, game.getActionPhaseOrder().get(1));
  }

  @Test
  void advanceGamePhaseToPlanning() {
    threePlayerGame.addPlayer("gino", TowerColor.BLACK);
    Player franco = threePlayerGame.getPlayers().get(0);
    Player sberg = threePlayerGame.getPlayers().get(1);
    Player gino = threePlayerGame.getPlayers().get(2);
    franco.setPlayedCard(4); // franco plays (4, 2)
    sberg.setPlayedCard(9); // sberg plays (9, 5)
    gino.setPlayedCard(2); // gino plays (2, 1)
    // Advance to ACTION phase to trigger initialization of action phase order
    threePlayerGame.advance();
    threePlayerGame.advance();
    threePlayerGame.advance();

    // Expected order:
    // gino -> franco -> sberg
    assertEquals(gino, threePlayerGame.getActionPhaseOrder().get(0));
    assertEquals(franco, threePlayerGame.getActionPhaseOrder().get(1));
    assertEquals(sberg, threePlayerGame.getActionPhaseOrder().get(2));

    // Advance to PLANNING phase to trigger an update of the planning phase order
    // We need to advance 3 (turn phase count) times 3 (number of players): 9 times
    for (int i = 0; i < 9; i++)
      threePlayerGame.advance();
    // Expected order:
    // gino -> franco -> sberg
    assertEquals(gino, threePlayerGame.getPlanningPhaseOrder().get(0));
    assertEquals(franco, threePlayerGame.getPlanningPhaseOrder().get(1));
    assertEquals(sberg, threePlayerGame.getPlanningPhaseOrder().get(2));
  }

  @Test
  void simpleAdvanceTurnPhase() {
    game.addPlayer("gino", TowerColor.BLACK);

    game.simpleAdvanceTurnPhase();
    assertEquals(TurnPhase.MOVING, game.getTurnPhase());
    game.simpleAdvanceTurnPhase();
    assertEquals(TurnPhase.PICKING, game.getTurnPhase());
    game.simpleAdvanceTurnPhase();
    assertEquals(TurnPhase.PLACING, game.getTurnPhase());
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
    GameState spiedGame = spy(game);
    Player p1 = spy(spiedGame.getPlayers().get(0));
    Player p2 = spy(spiedGame.getPlayers().get(1));
    players.add(p1);
    players.add(p2);


    p1.getDashboard().getTowers().count = 3;
    p2.getDashboard().getTowers().count = 2;

    doReturn(players).when(spiedGame).getPlayers();

    assertEquals(TowerColor.BLACK, spiedGame.getWinner().get());
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
}