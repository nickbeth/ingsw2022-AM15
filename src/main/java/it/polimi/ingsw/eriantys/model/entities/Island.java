package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.*;
import org.tinylog.Logger;

public class Island {
  private Students students = students = new Students();
  private Towers towers = new Towers();
  private boolean isLocked = false;

  /**
   * Initialize island with a student on
   * @param studentColor
   */
  public Island(HouseColor studentColor) {
    // Initializing island with one student on
    towers.count = 0;
    towers.color = null;
    students.addStudent(studentColor);
    Logger.debug("Island created");
  }

  public Island() {
    towers.count = 0;
    towers.color = null;
    Logger.debug("Island created");
  }

  public void addStudents(Students s) {
    students.addStudents(s);
    Logger.debug("Students added on Island");
  }

  public void setTowerColor(TowerColor towerColor) {
    towers.color = towerColor;
  }

  public void setTowerCount(int count) {
    towers.count = count;
  }

  public void setLocked(boolean lock) {
    isLocked = lock;
  }

  public Students getStudents() {
    return students;
  }

  public int getTowerCount() {
    return towers.count;
  }

  public TowerColor getTowerColor() {
    return towers.color;
  }

  public boolean isLocked() {
    return isLocked;
  }
}
