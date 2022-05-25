package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.NETWORK_ERROR;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.EXPERT;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.NORMAL;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  private boolean gameCodeExists = false;

  public MenuCreateOrJoin(CliController controller) {
    this.controller = controller;
    controller.addListener(this, GAMEINFO_EVENT.tag);
    controller.addListener(this, NETWORK_ERROR.tag);
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
      greenLight = false;
      showOptions(out);
      switch (in.nextLine()) {
        // Create a new game
        case "1" -> {
          gameCodeExists = false;
          showCreateOptions(in, out);
          waitForGreenLight();
          done = false;
          if (gameCodeExists) {
            out.println("Gamecode already exists");
            done = true;
          }
        }
        // Join to a game
        case "2" -> {
          gameCodeExists = true;
          showJoinOptions(in, out);
          waitForGreenLight();
          done = false;
          if (!gameCodeExists) {
            out.println("Gamecode does not exist");
            done = true;
          }
        }
        default -> out.println("Invalid choice");
      }
    }
    while (!done);
  }

  @Override
  public Menu next() {
    return new MenuGameInfo(controller);
  }

  private void showCreateOptions(Scanner in, PrintStream out) {
    boolean invalid;
    GameMode mode;
    String playersCount;

    // Get GameMode
    do {
      out.println("Enter the game mode: " +
              "\n1 - " + NORMAL +
              "\n2 - " + EXPERT);
      String choice = in.nextLine();
      invalid = !choice.equals("1") && !choice.equals("2");
      mode = choice.equals("1") ? NORMAL : EXPERT;
      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    // Get playersCount
    do {
      out.print("Enter the number of players: ");
      playersCount = in.nextLine();
      invalid = Integer.parseInt(playersCount) < 2 || Integer.parseInt(playersCount) > 4;
      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    controller.sender().sendCreateGame(Integer.parseInt(playersCount), mode);
  }

  private void showJoinOptions(Scanner in, PrintStream out) {
    while (true) {
      out.print("Enter the game code: ");
      try {
        controller.sender().sendJoinGame(GameCode.parseCode(in.nextLine()));
        break;
      } catch (GameCode.GameCodeException e) {
        out.print("Invalid game code: " + e.getMessage());
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived " + evt.getPropertyName());
    greenLight = true;

    gameCodeExists = evt.getPropertyName().equals(GAMEINFO_EVENT.tag);
    Logger.trace("Gamecode: " + gameCodeExists);
    if (gameCodeExists) {
      controller.removeListener(this, GAMEINFO_EVENT.tag);
      controller.removeListener(this, NETWORK_ERROR.tag);
    }
  }
}
