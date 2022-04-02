package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


class PlayingFieldTest {
  PlayingField p;

  @BeforeEach
  public void setUp() {
    p = new PlayingField(RuleBook.makeRules(GameMode.NORMAL,2));
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
}