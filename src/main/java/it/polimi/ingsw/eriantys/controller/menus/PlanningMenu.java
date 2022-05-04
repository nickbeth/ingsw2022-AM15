package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.PickAssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class PlanningMenu implements Menu {
  @Override
  public void commandMenu(ActionInvoker invoker, String playerNickname, GameState gameState, String action) {

    do {
      if (!gameState.isTurnOf(playerNickname)) {
//        show("Not your turn dumbass");
      } else {
        switch (action) {
          case "pick assistant" -> {
            String cardIndex = "0"; // get input?? non così
            invoker.executeAction(new PickAssistantCard(Integer.parseInt(cardIndex)));
          }
          case "anotherCase" -> {
            System.out.println();
            System.out.println();
          }
          default -> {} //show("Invalid command.");
        }
        if (gameState.checkWinCondition())
          gameState.getWinner();

      }
    } while (gameState.getGamePhase() == GamePhase.PLANNING);
  }
}
