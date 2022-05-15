package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Cloud;

import java.io.PrintStream;


public class CloudView extends View {
  private Cloud cloud;

  public CloudView(Cloud cloud) {
    this.cloud = cloud;
  }

  @Override
  public void draw(PrintStream o) {
    (new StudentsView(cloud.getStudents())).draw(System.out);
  }
}
