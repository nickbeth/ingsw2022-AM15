package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class Students extends Slot implements Serializable {
  protected EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

  /**
   * Initialize to zero all kind of students
   */
  public Students() {
    Arrays.stream((HouseColor.values())).forEach(color -> students.put(color, 0));
  }

  /**
   * Initialize to specific amount of students
   *
   * @param s Initial students
   */
  public Students(Students s) {
    Arrays.stream(HouseColor.values()).forEach(color -> students.put(color, s.getCount(color)));
  }

  /**
   * Adds the given amount of students
   *
   * @param s Amount of students to add
   */
  public void addStudents(Students s) {
    students.forEach((color, value) -> students.put(color, value + s.getCount(color)));
  }

  /**
   * Adds a single student with the given color
   */
  public void addStudent(HouseColor color) {
    students.put(color, students.get(color) + 1);
  }

  public void addStudents(HouseColor color, int amount) {
    students.put(color, students.get(color) + amount);
  }

  /**
   * Sets the amount of all students of this instance to the amount of the given instance
   */
  public void setStudents(Students s) {
    students.forEach((color, amount) -> students.put(color, s.getCount(color)));
  }

  /**
   * Removes one student with the given color
   *
   * @param color tells which student to remove
   * @return boolean
   */
  public boolean tryRemoveStudent(HouseColor color) {
    if (students.get(color) == 0) {
      modelLogger.warn("No students to remove. {} {}", color, students.get(color));
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

  /**
   * @return Returns true if there are more students of the given color than the given amount
   */
  public boolean hasEnough(HouseColor color, int amount) {
    return students.get(color) >= amount;
  }

  public boolean containsExactly(Students s) {
    for (var color : HouseColor.values()) {
      if (getCount(color) != s.getCount(color))
        return false;
    }
    return true;
  }

  public boolean contains(Students s) {
    for (var color : HouseColor.values()) {
      if (getCount(color) < s.getCount(color))
        return false;
    }
    return true;
  }

  /**
   * Checks if there's no students left
   *
   * @return True if Students is empty, false otherwise
   */
  public boolean isEmpty() {
    for (HouseColor c : HouseColor.values()) {
      if (students.get(c) != 0)
        return false;
    }
    return true;
  }

  /**
   * Returns the number of students of the given color
   *
   * @return Number of students of that color
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

  public LinkedList<HouseColor> toLinkedList() {
    LinkedList<HouseColor> list = new LinkedList<>();
    for (HouseColor c : HouseColor.values()) {
      for (int i = 0; i < students.get(c); i++) {
        list.add(c);
      }
    }
    return list;
  }

  /**
   * Returns a deep copy of this Students object
   * @return a Students object with the same amount of students as the one this method is called on
   */
  public Students getCopy() {
    Students copy = new Students();
    copy.setStudents(this);
    return copy;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    students.forEach((color, amount) ->
        s.append("\n").append(color.toString(), 0, 3).append(" - ").append(amount));
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
