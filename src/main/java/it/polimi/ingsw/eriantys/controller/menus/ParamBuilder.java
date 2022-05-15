package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public class ParamBuilder {
  private Students studentsToMove = new Students();

  public Students getStudentsToMove() {
    return studentsToMove;
  }

  public void addStudentColor(HouseColor color, int amount) {
    for (int i = 0; i < amount; i++) {
      studentsToMove.addStudent(color);
    }
  }

  public void flushStudentToMove() {
    studentsToMove = new Students();
  }
}
