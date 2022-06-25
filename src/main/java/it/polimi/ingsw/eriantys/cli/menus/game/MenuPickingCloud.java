package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CloudsView;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;

public class MenuPickingCloud extends MenuGame {
  public MenuPickingCloud() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    showViewOptions(out);

    if (isMyTurn()) {
      if (game().isLastPlayer(me()))
        out.println("You're the last player");
      out.println("Q - Pick cloud");
    }
    out.print("Make a choice: ");
  }

  @Override
  public MenuEnum show() {

    while (true) {

      String choice = getNonBlankString();

      handleViewOptions(choice);
      if (handleDisconnection(choice))
        return MenuEnum.CREATE_OR_JOIN;

      if (isMyTurn()) {
        switch (choice) {
          case "Q", "q" -> {
            if (!game().getTurnPhase().equals(TurnPhase.PICKING)) {
              clientLogger.warn("I'm not in right phase");
              break;
            }

            // Show clouds
            new CloudsView(clouds()).draw(out);

            // Gets cloud index
            out.println("Choose cloud index: ");
            int cloudIndex = getNumber() - 1; // Index correction

            // Send action PickCloud
            if (controller.sender().sendPickCloud(cloudIndex)) {
              waitForGreenLight();
              return MenuEnum.PICK_ASSISTANT;
            }
            out.println(colored("Invalid input parameters", RED));
            showOptions();
          }
          default -> {
          }
        }
      }
    }
  }

  private boolean amILastPlayer() {
    return game().isLastPlayer(me(), GamePhase.ACTION);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}

