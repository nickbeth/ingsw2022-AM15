package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.cli.views.GameLobbyView;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.GAMEINFO_EVENT;

public class MenuGameInfo extends Menu {
  public MenuGameInfo(CliController controller) {
    this.controller = controller;
    controller.addListener(this, GAMEINFO_EVENT.tag);
  }

  @Override
  protected void showOptions(PrintStream out) {
    (new GameLobbyView(controller.getGameInfo())).draw(out);
    out.println("C - Choose a tower color");
    out.println("S - Start game");
    //todo leave game?
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;

    do {
      greenLight = false;
      showOptions(out);
      switch (in.nextLine()) {
        //choose a tower color for the lobby
        case "C", "c" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          out.println("Choose tower color: ");
          out.println("1 - WHITE");
          out.println("2 - BLACK");
          out.println("3 - GRAY");
          int team = -1;
          TowerColor towerColor;
          try {
            team = in.nextInt();
          } catch (InputMismatchException e) {
            out.println("Input must be a number");
            return;
          }
          try {
            towerColor = new ArrayList<>(Arrays.asList(TowerColor.values())).get(team - 1);
          } catch (IndexOutOfBoundsException e) {
            out.println("Invalid number");
            return;
          }
          if (!controller.sender().sendSelectTower(towerColor)) {
            out.println("Maximum of " + towerColor + " players already reached");
            return;
          }
          waitForGreenLight();
        }
        //starts and initiates a game
        case "S", "s" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          if (!controller.sender().sendStartGame()) {
            out.println("There are not enough players with a chosen tower color to start");
            return;
          }
          waitForGreenLight();
          done = true;
        }
        default -> out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuPickAssistantCard(controller);
  }
}
