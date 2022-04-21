package it.polimi.ingsw.eriantys.model.entities;

import org.tinylog.Logger;


public class Cloud {
  private Students students;

  public Cloud(Students students) {
    this.students = new Students(students);
  }

  public Students getStudents() {
    return students;
  }

  public void setStudents(Students s) {
    students.setStudents(s);
    Logger.debug("Students cloud set");
  }

  public boolean isEmpty(){
    return students.isEmpty();
  }
}
