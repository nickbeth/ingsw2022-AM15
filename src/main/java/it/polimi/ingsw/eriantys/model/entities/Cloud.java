package it.polimi.ingsw.eriantys.model.entities;

import java.io.Serializable;

public class Cloud implements Serializable {
  private Students students;

  public Cloud(Students students) {
    this.students = new Students(students);
  }

  public Students getStudents() {
    return students;
  }

  public void setStudents(Students s) {
    students.setStudents(s);
  }

  public boolean isEmpty(){
    return students.isEmpty();
  }
}
