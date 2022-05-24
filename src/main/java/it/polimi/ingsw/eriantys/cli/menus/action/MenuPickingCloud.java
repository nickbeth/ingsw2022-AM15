package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.cli.views.CloudsView;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuPickingCloud extends Menu {
  public MenuPickingCloud(CliController controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions();
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      out.println("Q - Pick cloud");
    }
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;

    do {
      showOptions(out);
      switch (in.nextLine()) {
        case "Q", "q" -> {
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PICKING))
            break;
          out.println("Choose cloud index: ");
          (new CloudsView(controller.getGameState().getPlayingField().getClouds())).draw(out);

          if (!controller.sender().sendPickCloud(0)) {
            out.println("Invalid input parameters");
            return;
          }
          done = true;
        }
        default -> out.println("Choose a valid option");
      }
    } while (!done);
  }

  //todo pensare bene al next menu
  @Override
  public Menu next() {
    return new MenuPickAssistantCard(controller);
  }

}
