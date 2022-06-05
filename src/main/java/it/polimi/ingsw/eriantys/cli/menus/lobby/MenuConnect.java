package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.network.Client;

/**
 * Asks the user for server's address and port
 */
public class MenuConnect extends Menu {
  @Override
  protected void showOptions() {
    out.println("\nChoose server:");
    out.println("1 - Manually configure socket");
    out.println("ANY_KEY - Use default options (localhost:1234)");
  }

  /**
   * Gets the socket from user
   */
  @Override
  public MenuEnum show() {
    String address;
    String choice;
    int port;

    while (true) {
      address = "localhost";
      port = Client.DEFAULT_PORT;

      showOptions();

      out.print("Make a choice: ");
      choice = getKeyboardInput();
      if (choice.equals("1")) {
        out.print("Enter the IP address of the server: ");
        address = getNonBlankString();

        out.print("Enter the port the server is running on: ");
        port = getNumber();
      }

      // If it succeeds to connect goes on
      if (controller.connect(address, port)) {
        return MenuEnum.NICKNAME;
      } else {
        out.println("Failed to connect to the server");
      }
    }
  }
}
