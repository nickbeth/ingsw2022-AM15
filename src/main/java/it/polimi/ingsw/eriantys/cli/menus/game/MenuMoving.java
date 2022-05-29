package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;

public class MenuMoving extends MenuGame {
  public MenuMoving() {
    super();
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions(out);
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      out.println("T - Move mother nature");
    }
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {

    while (true) {
      showOptions(out);

      String choice = getNonBlankString(in, out);

      handleViewOptions(choice, out);
      switch (choice) {
        // Move mother nature a certain amount
        case "T", "t" -> {
          // Check of the Turn phase
          if (!game.getTurnPhase().equals(TurnPhase.MOVING))
            break;

          // Shows islands
          out.println("Playing Field: ");
          new IslandsView(islands, motherPosition).draw(out);

          // Gets the amount
          out.println("Insert the amount of mother nature movements: ");
          int amount = getNumber(in, out);

          // Send action
          if (!controller.sender().sendMoveMotherNature(amount)) {
            waitForGreenLight();
            return MenuEnum.PICKING_CLOUD;
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

    clearConsole();

    new IslandsView(islands, motherPosition).draw(out);
  }
}
