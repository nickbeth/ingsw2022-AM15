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

    if (isMyTurn()) {
      out.println("Q - Choose assistant card");
    }
    out.print("Make option: ");
  }

  @Override
  public MenuEnum show() {

    while (true) {

      String choice = getNonBlankString();

      handleViewOptions(choice);

      if (isMyTurn()) {
        switch (choice) {

          // Choose assistant card
          case "Q", "q" -> {
            new AssistantCardsView(me).draw(out);

            out.print("Choose card index: ");

            int index;
            while (true) {
              index = getNumber() - 1;
              if (index < me.getCards().size() && index >= 0)
                break;
              out.print("Choose a valid card: ");
            }

            if (controller.sender().sendPickAssistantCard(index)) {
              waitForGreenLight();
              return MenuEnum.PLACING;
            }
            out.println("Someone else already played this card.");
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
