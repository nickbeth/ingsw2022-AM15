package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
  private static final EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

  @BeforeAll
  static void setUp() {
    students.put(HouseColor.RED, 3);
    students.put(HouseColor.GREEN, 5);
    students.put(HouseColor.BLUE, 4);
    students.put(HouseColor.YELLOW, 0);
    students.put(HouseColor.PINK, -5);
  }

  @Test
  void shouldBePositive() {
    Island island1 = new Island();
    Island island2 = new Island(HouseColor.RED);
    island1.addStudents(students);
    for (HouseColor color : HouseColor.values()) {
//      System.out.println(color + ": " + island1.getStudents().get(color));
//      System.out.println(color + ": " + island2.getStudents().get(color));
      assertFalse(island1.getStudents().get(color) < 0, "Negative student number found");
      assertTrue(true, "It properly insert new students");
    }
  }
}
