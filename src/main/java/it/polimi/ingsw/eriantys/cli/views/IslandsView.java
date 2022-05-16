package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Island;

import java.io.PrintStream;
import java.util.List;

public class IslandsView extends View {
  private List<Island> islands;
  private int motherNaturePos;

  public IslandsView(List<Island> islands, int motherNaturePos) {
    this.islands = islands;
    this.motherNaturePos = motherNaturePos;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(PrintStream o) {
    for (int i = 0; i < islands.size(); i++) {
      o.println("Island - " + i);
      (new IslandView(islands.get(i))).draw(o);
      if(motherNaturePos == i) o.print(" <- MN");
      o.print("\n");
    }
    o.print("MN: Mother nature pawn position\n");
  }
}
