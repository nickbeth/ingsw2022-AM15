package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class PlayingFieldTest {
  PlayingField p;

  @BeforeEach
  public void setUp() {
    p = new PlayingField(RuleBook.makeRules(GameMode.NORMAL, 2));
  }


  @Test
  public void moveMotherNature() {
    p.moveMotherNature(12);
    assertEquals(0, p.getMotherNaturePosition());
    p.moveMotherNature(3);
    assertEquals(3, p.getMotherNaturePosition());
  }

  @Test
  public void refillClouds() {
  }

  @Test
  public void hasProfessor() {
  }

  @Test
  public void setProfessorHolder() {
  }


  @Test
  void mergeIslands() {
    //1
    p.getIsland(11).setTowerColor(TowerColor.BLACK);
    p.getIsland(0).setTowerColor(TowerColor.BLACK);
    p.getIsland(1).setTowerColor(TowerColor.BLACK);
    p.getIsland(11).setTowerCount(1);
    p.getIsland(0).setTowerCount(1);
    p.getIsland(1).setTowerCount(1);
    ArrayList<Island> oldIslands = new ArrayList<>();
    for (int i = 0; i < p.getIslandsAmount(); i++)
      oldIslands.add(p.getIsland(i));
    p.mergeIslands(0);

    assertSame(oldIslands.get(10), p.getIsland(9));
    assertSame(oldIslands.get(0), p.getIsland(0));
    assertSame(oldIslands.get(3), p.getIsland(2));
    assertEquals(3, p.getIsland(0).getTowerCount());
    //2
    setUp();
    p.getIsland(2).setTowerColor(TowerColor.BLACK);
    p.getIsland(1).setTowerColor(TowerColor.BLACK);
    p.getIsland(0).setTowerColor(TowerColor.BLACK);
    p.getIsland(2).setTowerCount(1);
    p.getIsland(1).setTowerCount(1);
    p.getIsland(0).setTowerCount(1);
    p.moveMotherNature(1);
    oldIslands.clear();
    for (int i = 0; i < p.getIslandsAmount(); i++)
      oldIslands.add(p.getIsland(i));
    p.mergeIslands(1);

    assertSame(oldIslands.get(1), p.getIsland(0));
    assertSame(oldIslands.get(3), p.getIsland(1));
    assertEquals(0, p.getMotherNaturePosition());
    assertEquals(3, p.getIsland(0).getTowerCount());

    //new test on shorter islands List
    p.moveMotherNature(1);
    p.getIsland(1).setTowerColor(TowerColor.BLACK);
    p.getIsland(1).setTowerCount(1);
    oldIslands.clear();
    for (int i = 0; i < p.getIslandsAmount(); i++)
      oldIslands.add(p.getIsland(i));
    p.mergeIslands(1);
    assertSame(oldIslands.get(1), p.getIsland(0));
    assertEquals(0, p.getMotherNaturePosition());
    assertEquals(4, p.getIsland(0).getTowerCount());
    //3
    setUp();
    p.getIsland(2).setTowerColor(TowerColor.BLACK);
    p.getIsland(1).setTowerColor(TowerColor.WHITE);
    p.getIsland(0).setTowerColor(TowerColor.BLACK);
    p.getIsland(2).setTowerCount(1);
    p.getIsland(1).setTowerCount(1);
    p.getIsland(0).setTowerCount(1);
    oldIslands.clear();
    for (int i = 0; i < p.getIslandsAmount(); i++)
      oldIslands.add(p.getIsland(i));
    p.mergeIslands(1);
    for (int i = 0; i < p.getIslandsAmount(); i++)
      assertSame(oldIslands.get(i), p.getIsland(i));
  }
  
  @Test
  void getMostInfluential() {
    p.getIsland(2).setTowerColor(TowerColor.BLACK);
    p.getIsland(2).setTowerCount(1);
    p.getIsland(2).getStudents().addStudent(HouseColor.RED);
    p.setProfessorHolder(TowerColor.BLACK, HouseColor.RED);
    Optional<TowerColor> result = p.getMostInfluential(2);
    assertTrue(result.isPresent());
    assertEquals(TowerColor.BLACK, result.get());

    setUp();
    p.getIsland(2).getStudents().addStudent(HouseColor.RED);
    result = p.getMostInfluential(2);
    assertFalse(result.isPresent());

    setUp();
    p.getIsland(2).getStudents().addStudent(HouseColor.RED);
    p.getIsland(2).getStudents().addStudent(HouseColor.RED);
    p.getIsland(2).getStudents().addStudent(HouseColor.RED);
    p.setProfessorHolder(TowerColor.BLACK, HouseColor.RED);
    result = p.getMostInfluential(2);
    assertTrue(result.isPresent());
    assertEquals(TowerColor.BLACK, result.get());

  }
}