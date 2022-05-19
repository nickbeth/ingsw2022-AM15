package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.controller.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuGameInfo extends Menu {
  public MenuGameInfo(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    System.out.println("C - Choose a tower color");
    System.out.println("S - Start game");
    //todo leave game?
    //showViewOptions();
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        //choose a tower color for the lobby
        case "C", "c" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          System.out.println("Choose tower color: ");
          System.out.println("1 - WHITE");
          System.out.println("2 - BLACK");
          System.out.println("3 - GRAY");
          int team = -1;
          TowerColor towerColor;
          try {
            team = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Input must be a number");
            return;
          }
          try {
            towerColor = new ArrayList<>(Arrays.asList(TowerColor.values())).get(team - 1);
          } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid number");
            return;
          }
          if (!controller.sendSelectTower(towerColor)) {
            System.out.println("Maximum of " + towerColor + " players already reached");
            return;
          }
        }
        //starts and initiates a game
        case "S", "s" -> {
          if (controller.getGameInfo().getLobbyState() != GameInfo.LobbyState.WAITING)
            break;
          if (!controller.startGame()) {
            System.out.println("There are not enough players with a chosen tower color to start");
            return;
          }
          done = true;
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return new MenuPickAssistantCard(controller);
  }
}
