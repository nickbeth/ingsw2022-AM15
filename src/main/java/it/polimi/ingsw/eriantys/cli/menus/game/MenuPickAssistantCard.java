package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;
import it.polimi.ingsw.eriantys.cli.views.PlayersView;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;

public class MenuPickAssistantCard extends MenuGame {
  public MenuPickAssistantCard() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    new PlayersView(players(), rules()).draw(out);
    showViewOptions();

    if (isMyTurn()) {
      out.println("Q - Choose assistant card");
      out.println(baseSeparator, YELLOW);
    }
    out.print("Make option: ");
  }

  @Override
  public MenuEnum show() {

    while (true) {
      if (!gamePhase().equals(GamePhase.PLANNING)) {
        return null;
      }

      String choice;

      choice = getNonBlankString();

      handleViewOptions(choice);
      if (handleDisconnection(choice))
        return MenuEnum.CREATE_OR_JOIN;

      if (isMyTurn()) {
        switch (choice) {
          case "forced_advancement_to_next_menu" -> {
            return null;
          }

          // Choose assistant card
          case "Q", "q" -> {
            if (!gamePhase().equals(GamePhase.PLANNING)) {
              out.println("You're in the wrong phase.", RED);
              break;
            }
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
            out.println("Someone else already played this card.", RED);
            showOptions();
          }
          default -> showOptions();
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
