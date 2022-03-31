package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.*;

import java.util.EnumMap;

public class Island {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
  private Towers towers;
  private boolean isLocked = false;

  public Island(HouseColor studentColor) {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, 0);
    }
    towers.count = 0;
    towers.color = null;
    students.put(studentColor, 1);
  }

  public Island() {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, 0);
    }
    towers.count = 0;
    towers.color = null;
  }

  public void addStudents(EnumMap<HouseColor, Integer> s) {
    for (HouseColor color : HouseColor.values()) {
      students.put(color, Math.max(students.get(color) + s.get(color), 0));
    }
  }

  public void setTower(TowerColor towerColor) {
    towers.color = towerColor;
  }

  public void addTower() {
    towers.count++;
  }

  public void setLocked(boolean lock) {
    isLocked = lock;
  }

  public EnumMap<HouseColor, Integer> getStudents() {
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
