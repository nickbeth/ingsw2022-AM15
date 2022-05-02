package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.EnumMap;

public class Students extends Slot {
  protected EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

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
            .forEach(color -> students.put(color, s.getCount(color)));
  }

  /**
   * Adds the given amount of students
   *
   * @param s Amount of students to add
   */
  public void addStudents(Students s) {
    students.forEach(((color, value)
            -> students.put(color, value + s.getCount(color))));
  }

  /**
   * Adds a single student with the given color
   *
   * @param color
   */
  public void addStudent(HouseColor color) {
    students.put(color, students.get(color) + 1);
  }

  public void addStudents(HouseColor color, int amount) {
    students.put(color, students.get(color) + amount);
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

  public boolean tryRemoveStudents(Students s) {
    for (var color : HouseColor.values()) {
      // If even one amount of students is not enough
      if (students.get(color) < s.getCount(color)) {
        return false;
      }
    }
    for (var color : HouseColor.values()) {
      for (int i = 0; i < s.getCount(color); i++)
        tryRemoveStudent(color);
    }
    return true;
  }

  public boolean hasEnough(HouseColor color, int amount) {
    return students.get(color) >= amount;
  }

  public void setStudents(Students s) {
    students.forEach(((color, amount) ->
            students.put(color, s.getCount(color))));
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
   * Returns the number of students of the given color
   *
   * @param color
   * @return number of students of that color
   */
  public int getCount(HouseColor color) {
    return students.get(color);
  }

  /**
   * @return The total number of students in this Students object
   */
  public int getCount() {
    int count = 0;
    for (HouseColor c : HouseColor.values()) {
      count += students.get(c);
    }
    return count;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    students.forEach((c, v) ->
            s.append("\n").append(c).append(" - ").append(v));
    return s.toString();
  }

  @Override
  public boolean removeStudentsFromSlot(Students students) {
    return tryRemoveStudents(students);
  }

  @Override
  public void addStudentsToSlot(Students students) {
    addStudents(students);
  }
}
