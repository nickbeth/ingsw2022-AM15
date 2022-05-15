package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.cli.utils.Util;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;
import java.text.MessageFormat;

import static it.polimi.ingsw.eriantys.cli.utils.Util.*;

public class IslandView extends View {
  private Island island;

  public IslandView(Island island) {
    this.island = island;
  }

  @Override
  public void draw(PrintStream o) {
    StringBuilder s = new StringBuilder();

    for (var color : HouseColor.values()) {
      s.setLength(0);
      if (island.getStudents().getCount(color) != 0) {
        s.append("\t").append(color.toString())
                .append(MessageFormat.format(" ({0}):", island.getStudents().getCount(color)))
                .append(printCountStudents(island.getStudents().getCount(color)))
                .append("\n");
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
