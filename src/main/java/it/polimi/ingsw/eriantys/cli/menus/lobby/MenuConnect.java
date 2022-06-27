package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.network.Client;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;

/**
 * Asks the user for server's address and port
 */
public class MenuConnect extends Menu {
  public MenuConnect() {
    showOptions();
  }

  @Override
  protected void showOptions() {
    out.println("\nConnect to a server:");
    out.println("0 - Quit Game");
    out.println("1 - Manually configure socket");
    out.println("ANY_KEY - Use default socket options (localhost:1234)");
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

      switch (choice) {
        case "0" -> {
          return null;
        }

        case "1" -> {
          out.print("Enter the IP address of the server: ");
          address = getNonBlankString();

          out.print("Enter the port the server is running on: ");
          port = getNumber();
        }
        default -> {
        }
      }

      out.println(colored("Reaching server...", HouseColor.YELLOW));
      // If it succeeds to connect goes on
      if (controller.connect(address, port)) {
        out.println(colored("Connected", HouseColor.GREEN));
        return MenuEnum.NICKNAME;
      } else {
        out.println(colored("Failed to connect to the server", HouseColor.YELLOW));
      }
    }
  }
}