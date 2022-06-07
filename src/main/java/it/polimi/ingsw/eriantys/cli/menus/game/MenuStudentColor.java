package it.polimi.ingsw.eriantys.cli.menus.game;

import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;

public class MenuStudentColor {
  private void showOptions(PrintStream out) {
    out.println("1 - Pink");
    out.println("2 - Red");
    out.println("3 - Blue");
    out.println("4 - Yellow");
    out.println("5 - Green");
    out.print("Choose the color of the students you want to move:");
  }

  public void show(Scanner in, PrintStream out, ParamBuilder paramBuilder) {
    boolean done;

    showOptions(out);
    do {
      done = true;

      // Choose the color
      switch (in.nextLine()) {
        case "1" -> paramBuilder.setChosenColor(PINK);
        case "2" -> paramBuilder.setChosenColor(RED);
        case "3" -> paramBuilder.setChosenColor(BLUE);
        case "4" -> paramBuilder.setChosenColor(YELLOW);
        case "5" -> paramBuilder.setChosenColor(GREEN);
        default -> {
          out.print("Insert a valid option: ");
          done = false;
        }
      }
    } while (!done);
  }
}
