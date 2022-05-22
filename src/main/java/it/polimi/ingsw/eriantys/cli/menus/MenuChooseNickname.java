package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.cli.Menu;

import java.util.Scanner;

public class MenuChooseNickname extends Menu {
  public MenuChooseNickname(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    System.out.print("Enter your nickname: ");
  }

  @Override
  public void makeChoice() {
    Scanner s = new Scanner(System.in);
    String input;
    boolean done = false;

    do {
      showOptions();
      input = s.nextLine();
      if (input.isBlank()) {
        System.out.println("A nickname cannot be empty or blank");
      } else {
        // TODO: call controller to send a message
        done = true;
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return new MenuCreateOrJoin(controller);
  }
}
