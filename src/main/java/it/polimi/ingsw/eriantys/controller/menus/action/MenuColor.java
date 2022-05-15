package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuColor extends Menu {
  @Override
  public void showOptions() {
    System.out.println("1 - Pink");
    System.out.println("2 - Red");
    System.out.println("3 - Blue");
    System.out.println("4 - Yellow");
    System.out.println("5 - Green");
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done;
    HouseColor chosenColor = null;

    do {
      showOptions();
      done = true;

      // Choose the color
      switch (s.nextLine()) {
        case "1" -> chosenColor = HouseColor.PINK;
        case "2" -> chosenColor = HouseColor.RED;
        case "3" -> chosenColor = HouseColor.BLUE;
        case "4" -> chosenColor = HouseColor.YELLOW;
        case "5" -> chosenColor = HouseColor.GREEN;
        default -> {
          System.out.println("Insert a valid option");
          done = false;
        }
      }
    } while (!done);

    // Ask for amount
    int amount;
    try {
      System.out.print("Amount: ");
      amount = s.nextInt();
      paramBuilder.addStudentColor(chosenColor, amount);
    } catch (InputMismatchException e) {
      System.out.println("Insert a number");
    }
  }

  @Override
  public Menu nextMenu() {
    return null;
  }
}
