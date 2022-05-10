package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import org.tinylog.Logger;

import java.util.*;

public class StudentBag {
  private Students students;
  Random rand = new Random();

  public StudentBag() {
    students = new Students();
  }

  /**
   * Initializes the amount of students in the bag to a certain amount for every color
   *
   * @param amount
   */
  public void initStudents(int amount) {
    students.setStudents(new Students());
    for (int i = 0; i < amount; i++) {
      for (HouseColor c : HouseColor.values())
        students.addStudent(c);
    }
  }

  //qui solo per il testing?
  public Students getStudents() {
    return students;
  }

  public void addStudent(HouseColor color) {
    students.addStudent(color);
  }

  /**
   * Returns a random student from students
   *
   * @return HouseColor
   */
  public HouseColor takeRandomStudent() {
    HouseColor student;

    do {
      student = HouseColor.values()[rand.nextInt(HouseColor.values().length)];
      if (students.getCount(student) != 0) {
        if (students.tryRemoveStudent(student)) {
          return student;
        }
      }
    } while (!students.isEmpty());
    Logger.error("Missing students from bag. Game should've ended.");
    return null;
  }

  /**
   * Removes given students from bag
   */
  public boolean removeStudents(Students s) {
    // try to remove the students all at once
//    if (!this.students.tryRemoveStudents(s)) {
//      // if it can't do that (no students left in the bag) try to remove them one by one
//      Logger.warn("GAME SHOULD END. NO STUDENTS LEFT IN THE BAG");
//      for (HouseColor c : HouseColor.values()) {
//        int count = s.getCount(c);
//        while (count > 0) {
//          count--;
//          students.tryRemoveStudent(c);
//        }
//      }
//    }
    return this.students.tryRemoveStudents(s);
  }

  public boolean isEmpty() {
    return students.isEmpty();
  }
}
