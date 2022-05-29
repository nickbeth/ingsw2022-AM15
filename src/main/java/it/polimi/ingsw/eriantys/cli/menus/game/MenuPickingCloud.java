package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CloudsView;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.Controller.getController;

public class MenuPickingCloud extends MenuGame {
  public MenuPickingCloud() {
//    this.nextMenu = new MenuPickAssistantCard(controller);
    controller = getController();
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions(out);
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      out.println("Q - Pick cloud");
    }
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {

    while (true) {
      showOptions(out);
      out.print("Make a choice: ");
      switch (in.nextLine()) {
        case "Q", "q" -> {
          if (!game.getTurnPhase().equals(TurnPhase.PICKING))
            break;

          // Show clouds
          (new CloudsView(controller.getGameState().getPlayingField().getClouds())).draw(out);

          // Gets cloud index
          out.println("Choose cloud index: ");
          int cloudIndex = getNumber(in, out);

          // Send action
          if (controller.sender().sendPickCloud(cloudIndex)) {
            waitForGreenLight();
            return MenuEnum.PICK_ASSISTANT;
          }
          out.println("Invalid input parameters");
        }
        default -> out.println("Choose a valid option");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}

