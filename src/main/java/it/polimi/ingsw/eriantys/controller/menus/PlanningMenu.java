package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.Input;
import it.polimi.ingsw.eriantys.controller.Output;
import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.PickAssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class PlanningMenu implements Menu {
  @Override
  public void commandMenu(ActionInvoker invoker, String playerNickname, GameState game) {
    Input input = null;
    Output output = null;

    String command = input.getPlayerInput();
    do {
      if (!game.isTurnOf(playerNickname)) {
        output.show("Not your turn dumbass");
      } else {
        switch (command.toLowerCase()) {
          case "pick" -> {
            String cardIndex = input.getPlayerInput();
            invoker.executeAction(new PickAssistantCard(Integer.parseInt(cardIndex), playerNickname));
          }
          case "anotherCase" -> {
            System.out.println();
            System.out.println();
          }
          default -> {
            output.show("Invalid command.");
          }
        }
        if (game.checkWinCondition())
          game.getWinner();

      }
    } while (game.getGamePhase() == GamePhase.PLANNING);
  }
}
