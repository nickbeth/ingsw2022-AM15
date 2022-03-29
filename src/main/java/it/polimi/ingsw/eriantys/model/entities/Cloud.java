package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;

import java.util.EnumMap;

public class Cloud {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

  public Cloud(EnumMap<HouseColor, Integer> students) {
    for (HouseColor color : HouseColor.values()) {
      this.students.put(color, Math.max(students.get(color), 0));
    }
  }

  public EnumMap<HouseColor, Integer> getStudents() {
    return students;
  }

  public void setStudents(EnumMap<HouseColor, Integer> students) {
    for (HouseColor color : HouseColor.values()) {
      this.students.put(color, students.get(color));
    }
  }
}
