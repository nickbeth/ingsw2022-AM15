package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.*;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.EnumMap;

public class Island {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
  private Towers towers = new Towers();
  private boolean isLocked = false;

  public Island(HouseColor studentColor) {
    // Initializing island with one student on
    Arrays.stream(HouseColor.values()).forEach(color -> students.put(color, 0));
    towers.count = 0;
    towers.color = null;
    students.put(studentColor, 1);
  }

  public Island() {
    // Initializing island with no students on
    Arrays.stream(HouseColor.values()).forEach(color -> students.put(color, 0));
    towers.count = 0;
    towers.color = null;
  }

  public void addStudents(EnumMap<HouseColor, Integer> s) {
    students.forEach((color, value) -> {
      if(students.get(color) == 0 ) Logger.warn("In addStudents() No students");
      else students.put(color, value + s.get(color));
    });
    Logger.debug("Student added to entrance");
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
