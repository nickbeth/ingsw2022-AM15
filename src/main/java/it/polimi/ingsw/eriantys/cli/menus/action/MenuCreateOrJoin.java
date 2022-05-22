package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.menus.MenuGameInfo;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.cli.Menu;

import java.util.Scanner;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  public MenuCreateOrJoin(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    System.out.println("1 - Create a new game");
    System.out.println("2 - Join an existing game");
  }

  @Override
  public void makeChoice() {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        case "1" -> {
          showCreateOptions(s);
          done = true;
        }
        case "2" -> {
          showJoinOptions(s);
          done = true;
        }
        default -> System.out.println("Invalid choice");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return new MenuGameInfo(controller);
  }

  private void showCreateOptions(Scanner s) {
    System.out.print("Enter the game mode: ");
    s.nextLine();
    System.out.print("Enter the number of players: ");
    s.nextLine();
    // TODO: handle input and call controller
  }

  private void showJoinOptions(Scanner s) {
    System.out.print("Enter the game code: ");
    s.nextLine();
    // TODO: handle input and call controller
  }
}
