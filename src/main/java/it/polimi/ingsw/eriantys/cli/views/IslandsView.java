package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Island;

import java.io.PrintStream;
import java.util.List;

public class IslandsView extends View {
  private List<Island> islands;

  public IslandsView(List<Island> islands) {
    this.islands = islands;
  }

  @Override
  public void draw(PrintStream o) {
    for (int i = 0; i < islands.size(); i++) {
      System.out.println("Island - " + i);
      (new IslandView(islands.get(i))).draw(System.out);
    }
  }
}
