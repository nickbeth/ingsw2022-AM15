package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.controller.Controller;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuChooseNickname extends Menu {
  public MenuChooseNickname(Controller controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.print("Enter your nickname: ");
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    String input;
    boolean done = false;

    do {
      showOptions(out);
      input = in.nextLine();
      if (input.isBlank()) {
        out.println("A nickname cannot be empty or blank");
      } else {
        controller.sendNickname(input);
        done = true;
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuCreateOrJoin(controller);
  }
}
