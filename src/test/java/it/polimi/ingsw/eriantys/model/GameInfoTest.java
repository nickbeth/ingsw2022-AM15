package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {
  private GameInfo makeValidInfo() {
    GameInfo info = new GameInfo(4, GameMode.NORMAL);
    info.addPlayer("playerone", TowerColor.BLACK);
    info.addPlayer("playerdoe", TowerColor.BLACK);
    info.addPlayer("playerdree", TowerColor.GRAY);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makeInvalidInfo() {
    GameInfo info = new GameInfo(4, GameMode.NORMAL);
    info.addPlayer("playerone", TowerColor.BLACK);
    info.addPlayer("playerdoe", TowerColor.WHITE);
    info.addPlayer("playerdree", TowerColor.GRAY);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makePartialInfo() {
    GameInfo info = new GameInfo(4, GameMode.NORMAL);
    info.addPlayer("playerone", TowerColor.WHITE);
    info.addPlayer("playerdoe", TowerColor.WHITE);
    info.addPlayer("playerdree", null);
    info.addPlayer("playervor", TowerColor.GRAY);
    return info;
  }

  private GameInfo makeNullInfo() {
    GameInfo info = new GameInfo(3, GameMode.NORMAL);
    info.addPlayer("playerone", TowerColor.WHITE);
    info.addPlayer("playerdoe", TowerColor.BLACK);
    info.addPlayer("playerdree", null);
    return info;
  }

  @Test
  void isReady() {
    GameInfo info = makeValidInfo();
    assertTrue(info.isReady());

    info = makeInvalidInfo();
    assertFalse(info.isReady());

    info = makeNullInfo();
    assertFalse(info.isReady());
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

  @Test
  void equals() {
    GameInfo info1 = makeValidInfo();
    GameInfo info2 = makeValidInfo();
    assertEquals(info1, info2);

    info1 = makeInvalidInfo();
    info2 = makeInvalidInfo();
    assertEquals(info1, info2);

    info1 = makeNullInfo();
    info2 = makeNullInfo();
    assertEquals(info1, info2);

    info1 = makePartialInfo();
    info2 = makePartialInfo();
    assertEquals(info1, info2);
  }
}