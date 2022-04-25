package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.io_controls.Input;
import it.polimi.ingsw.eriantys.controller.io_controls.InputImpl;
import it.polimi.ingsw.eriantys.controller.io_controls.Output;
import it.polimi.ingsw.eriantys.controller.io_controls.OutputImpl;
import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.ActivateCCEffect;
import it.polimi.ingsw.eriantys.model.actions.ChooseCharacterCard;
import it.polimi.ingsw.eriantys.model.actions.MoveMotherNature;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class ActionMenu implements Menu {
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
          case "place students" -> {
          }
          case "move mother nature" -> {
            int numberOfIsland = Integer.parseInt(input.getPlayerInput());
            invoker.executeAction(new MoveMotherNature(playerNickname, numberOfIsland));
          }
          case "pick cloud" -> {
          }
          case "activate cc" -> {
            invoker.executeAction(new ChooseCharacterCard(0));
            (new CCMenu()).commandMenu(invoker, playerNickname, gameState);
          }
          default -> output.show("Invalid command.");
        }
        if (gameState.checkWinCondition())
          gameState.getWinner();

      }
    } while (gameState.getGamePhase() == GamePhase.ACTION);

  }

}
