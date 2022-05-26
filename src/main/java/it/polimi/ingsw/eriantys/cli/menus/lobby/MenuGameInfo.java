package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.cli.views.GameLobbyView;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;

public class MenuGameInfo extends Menu {
  public MenuGameInfo(CliController controller) {
    this.nextMenu = new MenuPickAssistantCard(controller);
    this.controller = controller;
    controller.addListener(this, GAMEINFO_EVENT.tag);
  }

  @Override
  protected void showOptions(PrintStream out) {
    (new GameLobbyView(controller.getGameInfo())).draw(out);
    out.println("1 - Choose a tower color");
    out.println("2 - Start game");
    out.println("0 - Quit Lobby");
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done;

    do {
      done = false;
      greenLight = false;
      showOptions(out);
      out.println("Make a choice: ");
      switch (in.nextLine()) {
        //choose a tower color for the lobby
        case "1" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          controller.sender().sendSelectTower(getTowerColor(in, out));
          waitForGreenLight();
        }
        //starts and initiates a game
        case "2" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          if (!controller.sender().sendStartGame()) {
            out.println("There are not enough players with a chosen tower color to start");
            break;
          }
          waitForGreenLight();
          done = true;
        }
        case "0" -> {
          nextMenu = new MenuCreateOrJoin(controller);
          return;
        }
        default -> out.println("Choose a valid option");
      }
    } while (!done);
  }

  private TowerColor getTowerColor(Scanner in, PrintStream out) {
    boolean valid = true;
    TowerColor towerColor = null;

    out.println("Choose tower color.");
    out.println("1 - WHITE");
    out.println("2 - BLACK");
    out.println("3 - GRAY");
    out.print("Make a choice: ");
    while (true) {
      int team = getNumber(in, out);
      try {
        towerColor = new ArrayList<>(Arrays.asList(TowerColor.values())).get(team - 1);
        return towerColor;
      } catch (IndexOutOfBoundsException e) {
        out.println("Invalid choice, insert again: ");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
  }
}
