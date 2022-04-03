package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.tinylog.Logger;

import java.util.*;

public class StudentBag {
  private Students students;

  public StudentBag() {
    students = new Students();
  }

  /**
   * initializes the student amount in students to a certain amount for every color
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
    Random rand = new Random();
    HouseColor student;

    do {
      student = HouseColor.values()[rand.nextInt(HouseColor.values().length)];
      if (students.getValue(student) != 0) {
        if (students.tryRemoveStudent(student)) {
          return student;
        }
      }
    } while (!students.isEmpty());
    Logger.error("Missing students from bag. Game should've ended.");
    return null;
  }

}
