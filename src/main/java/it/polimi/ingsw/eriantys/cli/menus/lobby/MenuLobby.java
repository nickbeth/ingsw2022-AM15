package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.InputHandler;
import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.GameLobbyView;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;

public class MenuLobby extends Menu {

  public MenuLobby() {
    eventsToBeListening.add(GAMEINFO_EVENT);
    eventsToBeListening.add(START_GAME);
    eventsToBeListening.add(ERROR);
    showOptions();
  }

  @Override
  protected void showOptions() {
    (new GameLobbyView(controller.getGameInfo(), controller.getGameCode())).draw(out);
    out.println("1 - Set WHITE");
    out.println("2 - Set BLACK");
    out.println("3 - Set GRAY");
    out.println("4 - Start game");
    out.println("0 - Quit Lobby");
    out.print("Make a choice: ");
  }

  @Override
  public MenuEnum show() throws InterruptedException {

    while (true) {
      String choice = getKeyboardInput();
      clientLogger.debug("Handling choice");

      switch (choice) {

        // Choose a tower color for the lobby
        case "1", "2", "3" -> {
          if (!controller.sender().sendSelectTower(getTowerColor(choice))) {
            out.printf(colored("Cannot choose %s.", getTowerColor(choice)), RED);
            showOptions();
            break;
          }
          waitForGreenLight();
        }

        // Starts and initiates a game
        case "4" -> {
          if (!controller.getGameInfo().isStarted()) {
            if (!controller.sender().sendStartGame()) {
              out.println(colored("There are not enough players with a chosen tower color to start", RED));
              showOptions();
              break;
            }
            waitForGreenLight();
          }
          return MenuEnum.PICK_ASSISTANT;
        }

        // Back button, exit lobby
        case "0" -> {
          controller.sender().sendQuitGame();
          return MenuEnum.CREATE_OR_JOIN;
        }

        default -> out.print("Choose a valid option: ");
      }
    }
  }

  private TowerColor getTowerColor(String choice) {
    Map<String, TowerColor> towerColorMap = new HashMap<>();

    towerColorMap.put("1", TowerColor.WHITE);
    towerColorMap.put("2", TowerColor.BLACK);
    towerColorMap.put("3", TowerColor.GRAY);

    return towerColorMap.get(choice);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    clearConsole();

    if (evt.getPropertyName().equals(START_GAME.tag)) {
      InputHandler.get().setLine("4");
      inputGreenLight = true;
      greenLight = true;
      return;
    }

    if (evt.getPropertyName().equals(GAMEINFO_EVENT.tag)) {
      showOptions();
    }
    greenLight = true;
  }
}
