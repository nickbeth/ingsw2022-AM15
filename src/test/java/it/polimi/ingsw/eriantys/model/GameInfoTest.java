package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameInfoTest {
  private GameInfo makeValidInfo() {
    GameInfo info = new GameInfo();
    info.setMode(GameMode.NORMAL);
    info.setMaxPlayerCount(4);
    info.addPlayer("playerone", TowerColor.BLACK);
    info.addPlayer("playerdoe", TowerColor.BLACK);
    info.addPlayer("playerdree", TowerColor.GRAY);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makeInvalidInfo() {
    GameInfo info = new GameInfo();
    info.setMode(GameMode.NORMAL);
    info.setMaxPlayerCount(4);
    info.addPlayer("playerone", TowerColor.BLACK);
    info.addPlayer("playerdoe", TowerColor.WHITE);
    info.addPlayer("playerdree", TowerColor.GRAY);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makePartialInfo() {
    GameInfo info = new GameInfo();
    info.setMode(GameMode.NORMAL);
    info.setMaxPlayerCount(4);
    info.addPlayer("playerone", TowerColor.WHITE);
    info.addPlayer("playerdoe", TowerColor.WHITE);
    info.addPlayer("playerdree", null);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makeNullInfo() {
    GameInfo info = new GameInfo();
    info.setMode(GameMode.NORMAL);
    info.setMaxPlayerCount(3);
    info.addPlayer("playerone", TowerColor.WHITE);
    info.addPlayer("playerdoe", TowerColor.BLACK);
    info.addPlayer("playerdree", null);
    return info;
  }

  @Test
  void isReady() {
    GameInfo info = makeValidInfo();
    assertTrue(info.start());

    info = makeInvalidInfo();
    assertFalse(info.start());

    info = makeNullInfo();
    assertFalse(info.start());
  }

  @Test
  void isTowerColorValid() {
    GameInfo info = makePartialInfo();
    assertTrue(info.isTowerColorValid("playerdree", TowerColor.BLACK));
    assertTrue(info.isTowerColorValid("playerdree", TowerColor.GRAY));
    assertFalse(info.isTowerColorValid("playerdree", TowerColor.WHITE));

    info = makeNullInfo();
    assertFalse(info.isTowerColorValid("playerdree", TowerColor.BLACK));
    assertFalse(info.isTowerColorValid("playerdree", TowerColor.WHITE));
    assertTrue(info.isTowerColorValid("playerdree", TowerColor.GRAY));
  }
}