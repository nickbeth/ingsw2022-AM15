package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.EnumMap;

public class Cloud {
  private EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

  public Cloud(EnumMap<HouseColor, Integer> students) {
    // Initializing cloud students
    Arrays.stream(HouseColor.values()).forEach(color -> {
              if (students.get(color) < 0) Logger.warn("Negative number of students in cloud");
              else this.students.put(color, students.get(color));
            }
    );
  }

  public EnumMap<HouseColor, Integer> getStudents() {
    return students;
  }

  public void setStudents(EnumMap<HouseColor, Integer> students) {
    Arrays.stream(HouseColor.values()).forEach(color -> {
              if (students.get(color) < 0) Logger.warn("Negative number of students in cloud");
              else this.students.put(color, students.get(color));
            }
    );
    Logger.debug("Students cloud set");
  }
}
