package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.menus.ParamBuilder;

import java.util.Scanner;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;

public class MenuStudentColor {
  public void showOptions() {
    System.out.println("1 - Pink");
    System.out.println("2 - Red");
    System.out.println("3 - Blue");
    System.out.println("4 - Yellow");
    System.out.println("5 - Green");
  }

  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done;

    do {
      showOptions();
      done = true;

      // Choose the color
      switch (s.nextLine()) {
        case "1" -> paramBuilder.setChosenColor(PINK);
        case "2" -> paramBuilder.setChosenColor(RED);
        case "3" -> paramBuilder.setChosenColor(BLUE);
        case "4" -> paramBuilder.setChosenColor(YELLOW);
        case "5" -> paramBuilder.setChosenColor(GREEN);
        default -> {
          System.out.println("Insert a valid option");
          done = false;
        }
      }
    } while (!done);
  }
}
