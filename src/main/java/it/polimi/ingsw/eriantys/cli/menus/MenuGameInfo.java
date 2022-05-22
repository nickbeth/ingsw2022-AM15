package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.cli.views.GameLobbyView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuGameInfo extends Menu {
  public MenuGameInfo(Controller controller) {
    this.controller = controller;
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
          if (!controller.sendSelectTower(towerColor)) {
            out.println("Maximum of " + towerColor + " players already reached");
            return;
          }
        }
        //starts and initiates a game
        case "S", "s" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          if (!controller.startGame()) {
            out.println("There are not enough players with a chosen tower color to start");
            return;
          }
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
