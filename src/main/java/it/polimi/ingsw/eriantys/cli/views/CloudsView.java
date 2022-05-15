package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Cloud;

import java.io.PrintStream;
import java.util.List;

public class CloudsView extends View {
  private List<Cloud> clouds;

  public CloudsView(List<Cloud> clouds) {
    this.clouds = clouds;
  }

  @Override
  public void draw(PrintStream o) {
    for (int i = 0; i < clouds.size(); i++) {
      System.out.println("Cloud - " + i);
      (new CloudView(clouds.get(i))).draw(System.out);
    }
  }
}
