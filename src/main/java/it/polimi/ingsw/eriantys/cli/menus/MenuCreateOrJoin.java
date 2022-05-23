package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  public MenuCreateOrJoin(CliController controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.println("1 - Create a new game");
    out.println("2 - Join an existing game");
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;

    do {
      showOptions(out);
      switch (in.nextLine()) {
        case "1" -> {
          showCreateOptions(in, out);
          done = true;
        }
        case "2" -> {
          showJoinOptions(in, out);
          done = true;
        }
        default -> out.println("Invalid choice");
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuGameInfo(controller);
  }

  private void showCreateOptions(Scanner in, PrintStream out) {
    out.print("Enter the game mode: ");
    in.nextLine();
    out.print("Enter the number of players: ");
    in.nextLine();
    // TODO: handle input and call controller
  }

  private void showJoinOptions(Scanner in, PrintStream out) {
    out.print("Enter the game code: ");
    in.nextLine();
    // TODO: handle input and call controller
  }
}
