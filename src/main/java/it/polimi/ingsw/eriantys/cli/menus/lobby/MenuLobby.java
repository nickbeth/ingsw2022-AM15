package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.GameLobbyView;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.awt.desktop.OpenURIEvent;
import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.*;

public class MenuLobby extends Menu {
  public MenuLobby() {
    controller.addListener(this, GAMEINFO_EVENT.tag);
    controller.addListener(this, PLAYER_CONNECTION_CHANGED.tag);
    controller.addListener(this, ERROR.tag);
  }

  @Override
  protected void showOptions(PrintStream out) {
    (new GameLobbyView(controller.getGameInfo(), controller.getGameCode())).draw(out);
    out.println("1 - Choose a tower color");
    out.println("2 - Start game");
    out.println("0 - Quit Lobby");
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {

    while (true) {
      showOptions(out);
      out.println("Make a choice: ");
      switch (in.nextLine()) {

        // Choose a tower color for the lobby
        case "1" -> {
//          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
//            break;
          controller.sender().sendSelectTower(getTowerColor(in, out));
          waitForGreenLight();
        }

        // Starts and initiates a game
        case "2" -> {
//          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
//            break;
          if (!controller.sender().sendStartGame()) {
            out.println("There are not enough players with a chosen tower color to start");
            break;
          }
          waitForGreenLight();
          return MenuEnum.PICK_ASSISTANT;
        }

        // Back button
        case "0" -> {
          return MenuEnum.CREATE_OR_JOIN;
        }

        default -> out.println("Choose a valid option");
      }
    }
  }

  private TowerColor getTowerColor(Scanner in, PrintStream out) {
    Map<Integer, TowerColor> towerColorMap = setupMap();

    out.println("Choose tower color.");
    towerColorMap.keySet().forEach(key ->
        out.println(key + " - " + towerColorMap.get(key)));
    out.print("Make a choice: ");
    while (true) {
      try {
        return towerColorMap.get(getNumber(in, out));
      } catch (Exception e) {
        out.println("Invalid choice, insert again: ");
      }
    }
  }

  private Map<Integer, TowerColor> setupMap() {
    Map<Integer, TowerColor> towerColorMap = new HashMap<>();
    towerColorMap.put(1, TowerColor.WHITE);
    towerColorMap.put(2, TowerColor.BLACK);
    towerColorMap.put(3, TowerColor.GRAY);
    return towerColorMap;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    clearConsole();

    if (evt.getPropertyName().equals(GAMEINFO_EVENT.tag)) {
//      System.out.println("A new player joined");
      new GameLobbyView(controller.getGameInfo(), controller.getGameCode()).draw(System.out);
    }
    greenLight = true;
  }
}
