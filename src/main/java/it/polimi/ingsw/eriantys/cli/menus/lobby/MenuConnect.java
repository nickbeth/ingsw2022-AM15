package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
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
    this.nextMenu = new MenuChooseNickname(controller);
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.println("\nChoose server:");
    out.println("1 - Manually configure socket");
    out.println("ANY_KEY - Use default options (localhost:1234)");
  }

  /**
   * Gets the socket from user
   *
   * @param in  The input stream the user input will be read from
   * @param out The output stream the output will be sent to
   */
  @Override
  public void show(Scanner in, PrintStream out) {
    String address;
    int port;
    boolean done = false;

    do {
      address = "localhost";
      port = Client.DEFAULT_PORT;

      showOptions(out);
      out.print("Make a choice: ");
      if (in.nextLine().equals("1")) {
        out.print("Enter the IP address of the server: ");
        address = getNonBlankString(in, out);

        out.print("Enter the port the server is running on: ");
        port = getNumber(in, out);
      }

      if (controller.connect(address, port)) {
        done = true;
      } else {
        out.println("Failed to connect to the server");
      }
    } while (!done);
  }
}
