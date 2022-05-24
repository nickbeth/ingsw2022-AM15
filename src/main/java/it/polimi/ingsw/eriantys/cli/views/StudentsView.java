package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;
import java.text.MessageFormat;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;

public class StudentsView extends View {
  private Students students;

  public StudentsView(Students students) {
    this.students = students;
  }

  @Override
  public void draw(PrintStream o) {
    StringBuilder s = new StringBuilder();

    for (var color : HouseColor.values()) {
      s.setLength(0);
      if (students.getCount(color) != 0) {
        s
                .append(MessageFormat.format(" ({0})", students.getCount(color)))
                .append(PADDING)
                .append(color.toString()).append(":")
                .append("\t")
                .append(printCountStudents(students.getCount(color)));
        o.append(printColored(s.toString(), color));
      }
    }
  }

  private String printCountStudents(int amount) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      s.append(STUDENT_CHAR).append(PADDING);
    }
    return s.toString();
  }
}
