package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.cli.menus.MenuEnum.PICK_ASSISTANT;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.EXPERT;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.NORMAL;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  private boolean errorEncountered = false;
  private boolean isGameAlreadyStarted = false;

  public MenuCreateOrJoin() {
    eventsToBeListening.add(GAMEINFO_EVENT);
    eventsToBeListening.add(START_GAME);
    eventsToBeListening.add(ERROR);
  }

  @Override
  protected void showOptions() {
    out.println("\n1 - Create a new game");
    out.println("2 - Join an existing game");
    out.println("ENTER - Default: NORMAL game with 2 players");
    out.println("0 - Back");
  }

  @Override
  public MenuEnum show() throws InterruptedException {
    String choice;

    while (true) {
      errorEncountered = true;


      showOptions();
      out.print("Make a choice: ");
      choice = getKeyboardInput();

      if (isGameAlreadyStarted) {
        return PICK_ASSISTANT;
      }

      switch (choice) {

        // Use default config
        case "" -> {
          controller.sender().sendCreateGame(2, NORMAL);
          waitForGreenLight();
          if (!errorEncountered) {
            return MenuEnum.LOBBY;
          }
        }

        // Create a new game
        case "1" -> {
          chooseGameSettings();
          waitForGreenLight();
          if (!errorEncountered) {
            return MenuEnum.LOBBY;
          }
          out.println("Game code already exists");
        }

        // Join to a game
        case "2" -> {
          // Gets the game code
          while (true) {
            try {
              out.print("Enter the game code: ");
              GameCode code = GameCode.parseCode(getNonBlankString());
              controller.sender().sendJoinGame(code);
              break;
            } catch (GameCode.GameCodeException e) {
              out.println(colored("Not a valid gameCode. Error message: " + e.getMessage(), RED));
            }
          }
          waitForGreenLight();

          if (isGameAlreadyStarted) {
            return PICK_ASSISTANT;
          }

          if (!errorEncountered) {
            return MenuEnum.LOBBY;
          }
//          out.println("Game code does not exist");
        }

        // Back button
        case "0" -> {
          out.println(colored("Disconnecting", YELLOW));
          out.println();
          controller.sender().sendQuitGame();
          controller.disconnect();
          return MenuEnum.CONNECTION;
        }
        default -> out.println("Invalid choice");
      }
    }
  }

  private void chooseGameSettings() {
    boolean invalid;
    GameMode mode;
    int playersCount;

    // Get GameMode
    do {
      out.printf("Modes: \n1 - %s \n2 - %s\nEnter the game mode: ", NORMAL, EXPERT);
      String choice = getNonBlankString();
      mode = choice.equals("1") ? NORMAL : EXPERT;
      invalid = !choice.equals("1") && !choice.equals("2");

      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    // Get playersCount

    do {
      out.print("Enter the number of players (must be 2, 3 or 4): ");
      playersCount = getNumber();
      invalid = playersCount < 2 || playersCount > 4;

      if (invalid) out.println("Enter a valid choice.");
    } while (invalid);

    controller.sender().sendCreateGame(playersCount, mode);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    if (evt.getPropertyName().equals(START_GAME.tag)) {
      out.print(colored("\nConnected to the game " + controller.getGameCode() + ".", GREEN));
      isGameAlreadyStarted = true;
      inputGreenLight = true;
    }

    if (evt.getPropertyName().equals(GAMEINFO_EVENT.tag)) {
      clientLogger.debug("Message from server. Valid option");
      errorEncountered = false;
    }
    greenLight = true;
  }
}
