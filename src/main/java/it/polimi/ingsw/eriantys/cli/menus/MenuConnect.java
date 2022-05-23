package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.network.Client;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Asks the user for server's address and port
 */
public class MenuConnect extends Menu {
  public MenuConnect(CliController controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    String input, address = null;
    int port = Client.DEFAULT_PORT;
    boolean done = false;

    do {
      out.print("Enter the IP address of the server (default: localhost): ");
      input = in.nextLine();
      if (!input.isBlank())
        address = input;

      out.print("Enter the port the server is running on (default: 1234): ");
      while (true) {
        input = in.nextLine();
        try {
          if (!input.isEmpty())
            port = Integer.parseInt(input);
          break;
        } catch (NumberFormatException e) {
          out.println("Invalid port, try again");
        }
      }

      if (controller.connect(address, port)) {
        done = true;
      } else {
        out.println("Failed to connect to the server");
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuChooseNickname(controller);
  }
}
