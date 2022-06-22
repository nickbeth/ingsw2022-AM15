package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;
import it.polimi.ingsw.eriantys.cli.views.PlayersView;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;

public class MenuPickAssistantCard extends MenuGame {
  public MenuPickAssistantCard() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    new PlayersView(players(), rules()).draw(out);
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
            new AssistantCardsView(me()).draw(out);

            out.print("Choose card index: ");

            int index;
            while (true) {
              index = getNumber() - 1;
              if (index < me().getCards().size() && index >= 0)
                break;
              out.print("Choose a valid card: ");
            }

            if (controller.sender().sendPickAssistantCard(index)) {
              waitForGreenLight();
              return MenuEnum.PLACING;
            }
            out.println(colored("Someone else already played this card.", RED));
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
