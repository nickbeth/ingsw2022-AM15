package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;

import java.beans.PropertyChangeEvent;

public class MenuPickAssistantCard extends MenuGame {
  public MenuPickAssistantCard() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    showViewOptions(out);
    if (controller.getGameState().isTurnOf(controller.getNickname())) {
      out.println("Q - Choose assistant card");
    } else {
      out.println("It's not your turn, you can see the state of the game tho.");
    }
  }

  @Override
  public MenuEnum show() {

    while (true) {

      String choice = getNonBlankString();

      handleViewOptions(choice);

      if (controller.getGameState().isTurnOf(controller.getNickname())) {
        switch (choice) {

          // Choose assistant card
          case "Q", "q" -> {
            new AssistantCardsView(currentPlayer).draw(out);

            out.print("Choose card index:");
            int index = getNumber() - 1;
            if (controller.sender().sendPickAssistantCard(index)) {
              waitForGreenLight();
              return MenuEnum.PLACING;
            }
            out.println("Invalid input parameters.");
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
