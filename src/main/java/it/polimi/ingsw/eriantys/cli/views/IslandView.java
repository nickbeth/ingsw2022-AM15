package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Island;

import java.io.PrintStream;

public class IslandView extends View {
  private Island island;

  public IslandView(Island island) {
    this.island = island;
  }

  @Override
  public void draw(PrintStream o) {
    (new StudentsView(island.getStudents())).draw(System.out);
  }
}
