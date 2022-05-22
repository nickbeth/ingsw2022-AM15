package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.menus.ParamBuilder;

import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;

public class MenuStudentColor {
  protected void showOptions(PrintStream out) {
    out.println("1 - Pink");
    out.println("2 - Red");
    out.println("3 - Blue");
    out.println("4 - Yellow");
    out.println("5 - Green");
  }

  public void show(Scanner in, PrintStream out, ParamBuilder paramBuilder) {
    boolean done;

    do {
      showOptions(out);
      done = true;

      // Choose the color
      switch (in.nextLine()) {
        case "1" -> paramBuilder.setChosenColor(PINK);
        case "2" -> paramBuilder.setChosenColor(RED);
        case "3" -> paramBuilder.setChosenColor(BLUE);
        case "4" -> paramBuilder.setChosenColor(YELLOW);
        case "5" -> paramBuilder.setChosenColor(GREEN);
        default -> {
          out.println("Insert a valid option");
          done = false;
        }
      }
    } while (!done);
  }
}
