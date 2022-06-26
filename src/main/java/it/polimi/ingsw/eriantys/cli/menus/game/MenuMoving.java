package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;
import static java.text.MessageFormat.format;

public class MenuMoving extends MenuGame {
  public MenuMoving() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    showViewOptions(out);

    if (isMyTurn()) {
      out.println("T - Move mother nature");
    }
    out.print("Make a choice: ");
  }

  @Override
  public MenuEnum show() {


    while (true) {
      if (!turnPhase().equals(TurnPhase.MOVING)) {
        // out.println(colored("You're in the wrong phase.", RED));
        return null;
      }

      String choice = getNonBlankString();

      handleViewOptions(choice);
      if (handleDisconnection(choice))
        return MenuEnum.CREATE_OR_JOIN;

      if (isMyTurn()) {
        switch (choice) {
          case "forced_advancement_to_next_menu" -> {
            return null;
          }

          // Move mother nature a certain amount
          case "T", "t" -> {
            // Check of the Turn phase
            if (!turnPhase().equals(TurnPhase.MOVING)) {
              out.println(colored("You're in the wrong phase.", RED));
              break;
            }

            // Shows islands
            out.println("Playing Field: ");
            islandsView().draw(out);

            // Gets the amount
            out.print(
                format("Insert the amount of mother nature movements (max {0}): ", me().getMaxMovement()));

            int amount;
            while (true) {
              amount = getNumber();
              if (amount <= me().getMaxMovement())
                break;
              out.print(format("Max movement are {0}. Insert again: ", me().getMaxMovement()));
            }

            // Send action
            if (controller.sender().sendMoveMotherNature(amount)) {
              waitForGreenLight();
              islandsView().draw(out);
              return MenuEnum.PICKING_CLOUD;
            }
            out.println(colored("Invalid input parameters. Valid movement:" + me().getMaxMovement() + ".", RED));
            showOptions();
          }

          default -> {
          }
        }
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
