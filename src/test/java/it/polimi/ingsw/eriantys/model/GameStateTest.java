package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    //no test required
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
}