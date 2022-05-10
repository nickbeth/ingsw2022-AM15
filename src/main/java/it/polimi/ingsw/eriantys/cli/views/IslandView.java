package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.cli.utils.BoxSymbols;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;

import static it.polimi.ingsw.eriantys.cli.utils.Util.*;

public class IslandView extends View {
  private Island island;

  public IslandView(Island island) {
    this.island = island;
  }

  @Override
  public void draw(PrintStream o) {
    int maxLenghtRow;
    for (var color : HouseColor.values()) {
      o.append(printCountStudents(island.getStudents().getCount(color), color));
    }
  }

  private String printCountStudents(int amount, HouseColor color) {
    StringBuilder o = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      o.append(getColorString(color))
              .append(STUDENT_CHAR)
              .append(PADDING);
    }
    return o.toString();
  }
}
