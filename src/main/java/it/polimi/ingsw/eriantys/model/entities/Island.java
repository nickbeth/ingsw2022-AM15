package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.actions.Slot;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

public class Island extends Slot {
  private Students students = new Students();
  private Towers towers = new Towers();
  private boolean isLocked = false;

  /**
   * Initialize island with a student on
   *
   * @param studentColor
   */
  public Island(HouseColor studentColor) {
    // Initializing island with one student on
    towers.count = 0;
    towers.color = null;
    students.addStudent(studentColor);
//    Logger.debug("Island created");
  }

  public Island() {
    towers.count = 0;
    towers.color = null;
//    Logger.debug("Island created");
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

  @Override
  public void removeStudentFromSlot(HouseColor color) {
    this.students.removeStudentFromSlot(color);
  }

  @Override
  public void addStudentToSlot(HouseColor color) {
    this.students.addStudentToSlot(color);
  }
}
