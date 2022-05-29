package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

public class MenuPickAssistantCard extends MenuGame {
  public MenuPickAssistantCard() {
    super();
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions(out);
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      System.out.println("A - Choose assistant card");
    }
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {

    while (true) {
      showOptions(out);
      String choice = getNonBlankString(in, out);

      handleViewOptions(choice, out);
      switch (choice) {

        // Choose assistant card
        case "A", "a" -> {
          (new AssistantCardsView(controller.getGameState().getPlayer(controller.getNickname()))).draw(System.out);

          System.out.print("Choose card index:");
          int index = getNumber(in, out);
          if (controller.sender().sendPickAssistantCard(index)) {
            waitForGreenLight();
            return MenuEnum.PLACING;
          }
          System.out.println("Invalid input parameters.");
          break;
        }
        default -> System.out.println("Choose a valid option");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
