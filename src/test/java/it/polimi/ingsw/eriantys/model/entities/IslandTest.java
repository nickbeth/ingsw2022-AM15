package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

  @Test
  void addStudents() {
    EnumMap<HouseColor, Integer> s = new EnumMap<>(HouseColor.class);
    Arrays.stream(HouseColor.values()).forEach(color -> s.put(color, 1));
    Island i = new Island();
    i.addStudents(s);
    assertTrue(i.getStudents().equals(s));
  }
}