package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.io_controls.Input;
import it.polimi.ingsw.eriantys.controller.io_controls.InputImpl;
import it.polimi.ingsw.eriantys.controller.io_controls.Output;
import it.polimi.ingsw.eriantys.controller.io_controls.OutputImpl;
import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.PickAssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class PlanningMenu implements Menu {
  @Override
  public void commandMenu(ActionInvoker invoker, String playerNickname, GameState gameState) {
    Input input = new InputImpl();
    Output output = new OutputImpl();

    String command = input.getPlayerInput();
    do {
      if (!gameState.isTurnOf(playerNickname)) {
        output.show("Not your turn dumbass");
      } else {
        switch (command.toLowerCase()) {
          case "pick assistant" -> {
            String cardIndex = input.getPlayerInput();
            invoker.executeAction(new PickAssistantCard(Integer.parseInt(cardIndex)));
          }
          case "anotherCase" -> {
            System.out.println();
            System.out.println();
          }
          default -> output.show("Invalid command.");
        }
        if (gameState.checkWinCondition())
          gameState.getWinner();

      }
    } while (gameState.getGamePhase() == GamePhase.PLANNING);
  }
}
