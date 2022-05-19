package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.CloudsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.controller.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.Scanner;

public class MenuPickingCloud extends Menu {
  public MenuPickingCloud(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      System.out.println("Q - Pick cloud");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        case "Q", "q" -> {
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PICKING))
            break;
          System.out.println("Choose cloud index: ");
          (new CloudsView(controller.getGameState().getPlayingField().getClouds())).draw(System.out);

          if (!controller.sendPickCloud(0)) {
            System.out.println("Invalid input parameters");
            return;
          }
          done = true;
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  //todo pensare bene al next menu
  @Override
  public Menu nextMenu() {
    return new MenuPickAssistantCard(controller);
  }

}
