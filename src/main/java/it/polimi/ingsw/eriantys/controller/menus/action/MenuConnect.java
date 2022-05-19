package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.network.Client;

import java.util.Scanner;

/**
 * Asks the user for server's address and port
 */
public class MenuConnect extends Menu {
  public MenuConnect(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    String input, address = null;
    int port = Client.DEFAULT_PORT;
    boolean done = false;

    do {
      System.out.print("Enter the IP address of the server (default: localhost): ");
      input = s.nextLine();
      if (!input.isBlank())
        address = input;

      System.out.print("Enter the port the server is running on (default: 1234): ");
      while (true) {
        input = s.nextLine();
        try {
          if (!input.isEmpty())
            port = Integer.parseInt(input);
          break;
        } catch (NumberFormatException e) {
          System.out.println("Invalid port, try again");
        }
      }

      if (controller.connect(address, port)) {
        done = true;
      } else {
        System.out.println("Failed to connect to the server");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return new MenuChooseNickname(controller);
  }
}
