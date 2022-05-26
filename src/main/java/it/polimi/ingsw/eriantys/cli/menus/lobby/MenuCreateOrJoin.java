package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.ERROR;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.EXPERT;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.NORMAL;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  private boolean errorEncountered = false;

  public MenuCreateOrJoin(CliController controller) {
    this.nextMenu = new MenuGameInfo(controller);
    this.controller = controller;
    controller.addListener(this, GAMEINFO_EVENT.tag);
    controller.addListener(this, ERROR.tag);
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.println("1 - Create a new game");
    out.println("2 - Join an existing game");
    out.println("0 - Back");
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done;

    do {
      done = false;
      greenLight = false;
      showOptions(out);
      out.print("Make a choice: ");
      switch (in.nextLine()) {
        // Create a new game
        case "1" -> {
          chooseGameSettings(in, out);
          waitForGreenLight();
          if (errorEncountered) {
            out.println("Gamecode already exists");
            break;
          }
          done = true;
        }
        // Join to a game
        case "2" -> {
          errorEncountered = true;

          // Gets the game code
          while (true) {
            try {
              out.print("Enter the game code: ");
              controller.sender().sendJoinGame(GameCode.parseCode(in.nextLine()));
              break;
            } catch (GameCode.GameCodeException e) {
              out.println("Not a valid gameCode. Error message: " + e.getMessage());
            }
          }
          waitForGreenLight();
          if (errorEncountered) {
            out.println("Gamecode does not exist");
            break;
          }
          done = true;
        }
        case "0" -> {
          nextMenu = new MenuChooseNickname(controller);
          return;
        }
        default -> out.println("Invalid choice");
      }
    }
    while (!done);
  }

  private void chooseGameSettings(Scanner in, PrintStream out) {
    boolean invalid;
    GameMode mode;
    int playersCount;

    // Get GameMode
    do {
      out.printf("Modes: \n1 - %s \n2 - %s\nEnter the game mode: ", NORMAL, EXPERT);
      String choice = getNonBlankString(in, out);
      mode = choice.equals("1") ? NORMAL : EXPERT;
      invalid = !choice.equals("1") && !choice.equals("2");

      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    // Get playersCount

    do {
      out.print("Enter the number of players (must be 2, 3 or 4): ");
      playersCount = getNumber(in, out);
      invalid = playersCount < 2 || playersCount > 4;

      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    controller.sender().sendCreateGame(playersCount, mode);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived " + evt.getPropertyName());
    greenLight = true;

    if (evt.getPropertyName().equals(GAMEINFO_EVENT.tag)) {
      errorEncountered = false;
      controller.removeListener(this, GAMEINFO_EVENT.tag);
      controller.removeListener(this, ERROR.tag);
    }
  }
}
