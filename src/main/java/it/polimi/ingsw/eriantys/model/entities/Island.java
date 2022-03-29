package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.*;

import java.util.EnumMap;

public class Island {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
  private int towerCount = 0;
  private TowerColor towerColor;
  private boolean isLocked = false;

  public Island(HouseColor studentColor) {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, 0);
    }
    students.put(studentColor, 1);
  }

  public Island() {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, 0);
    }
  }

  public void addStudents(EnumMap<HouseColor, Integer> s) {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, Math.max(students.get(color) + s.get(color),0));
    }
  }

  public void setTower(TowerColor towerColor) {
    this.towerColor = towerColor;
    towerCount++;
  }

  public void setLocked(boolean lock){
    this.isLocked = lock;
  }

  public EnumMap<HouseColor, Integer> getStudents() {
    return students;
  }

  public int getTowerCount() {
    return towerCount;
  }

  public TowerColor getTowerColor() {
    return towerColor;
  }

  public boolean isLocked() {
    return isLocked;
  }
}
