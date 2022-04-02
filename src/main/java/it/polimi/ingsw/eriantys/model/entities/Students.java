package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.EnumMap;

public class Students {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

  /**
   * Initialize to zero all kind of students
   */
  public Students() {
    Arrays.stream((HouseColor.values()))
            .forEach(color -> students.put(color, 0));
  }

  /**
   * Initialize to specific amount of students
   *
   * @param s Initial students
   */
  public Students(Students s) {
    Arrays.stream((HouseColor.values()))
            .forEach(color -> students.put(color, s.getValue(color)));
  }

  /**
   * Adds the given amount of students
   *
   * @param s Amount of students to add
   */
  public void addStudents(Students s) {
    students.forEach(((color, value)
            -> students.put(color, value + s.getValue(color))));
  }

  /**
   * Adds a single student with the given color
   *
   * @param color
   */
  public void addStudent(HouseColor color) {
    students.put(color, students.get(color) + 1);
  }

  /**
   * Removes one student with the given color
   *
   * @param color tells which student to remove
   * @return boolean
   */
  public boolean tryRemoveStudent(HouseColor color) {
    if (students.get(color) == 0) {
      Logger.warn("No students to remove. {} {}", color, students.get(color));
      return false;
    } else {
      students.put(color, students.get(color) - 1);
      return true;
    }
  }

  public void setStudents(Students s) {
    students.forEach(((color, amount) ->
            students.put(color, s.getValue(color))));
  }

  /**
   * Checks if there's no students left
   *
   * @return True if Students is empty, false otherwise
   */
  public boolean isEmpty() {
    for (HouseColor c : HouseColor.values()) {
      if (students.get(c) != 0) return false;
    }
    return true;
  }

  /**
   * Returns the number of students with the given color
   *
   * @param color
   * @return number of students with that color
   */
  public Integer getValue(HouseColor color) {
    return students.get(color);
  }


  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    students.forEach((c, v) -> s.append(c + " - " + v + "" + "\n"));
    return s.toString();
  }
}
