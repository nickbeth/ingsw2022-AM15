package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import static it.polimi.ingsw.eriantys.cli.utils.Util.*;
import java.io.PrintStream;

public class IslandView extends View {
  private Island island;

  public IslandView(Island island) {
    this.island = island;
  }

  @Override
  public void draw(PrintStream o) {
    StringBuilder s = new StringBuilder();
    (new StudentsView(island.getStudents())).draw(o);
    if(island.isLocked()) {
      s.append(PADDING).append("LOCKED");
      o.print(s);
    }
  }
}
