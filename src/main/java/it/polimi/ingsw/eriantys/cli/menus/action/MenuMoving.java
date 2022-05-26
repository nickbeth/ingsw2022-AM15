package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuMoving extends Menu {
  public MenuMoving(CliController controller) {
    this.nextMenu = new MenuPickingCloud(controller);
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions(out);
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      out.println("T - Move mother nature");
    }
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;

    do {
      showOptions(out);
      switch (in.nextLine()) {
        // Move mother nature a certain amount
        case "T", "t" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.MOVING))
            break;

          // Takes amount input and send the action
          int amount = -1;
          try {
            // Shows islands
            out.println("Playing Field: ");
            (new IslandsView(controller.getGameState().getPlayingField().getIslands(),
                controller.getGameState().getPlayingField().getMotherNaturePosition())).draw(out);
            out.println("Insert the amount of mother nature movements: ");
            amount = in.nextInt();
          } catch (InputMismatchException e) {
            out.println("Input must be a number");
          }
          //sends action
          if (!controller.sender().sendMoveMotherNature(amount)) {
            out.println("Invalid input parameters");
            return;
          }
        }
        default -> out.println("Choose a valid option");
      }
    } while (!done);
  }
}
