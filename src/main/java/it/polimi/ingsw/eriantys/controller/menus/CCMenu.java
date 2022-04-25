package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.io_controls.Input;
import it.polimi.ingsw.eriantys.controller.io_controls.InputImpl;
import it.polimi.ingsw.eriantys.controller.io_controls.Output;
import it.polimi.ingsw.eriantys.controller.io_controls.OutputImpl;
import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class CCMenu implements Menu {
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
          case " " -> {

          }
          case "move mother nature" -> {
          }
          case "pick cloud" -> {
          }
          case "activate cc" -> {
          }
          default -> System.out.println();
        }
      }
    } while (gameState.getTurnPhase() == TurnPhase.EFFECT);
  }
}
