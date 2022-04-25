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
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import javax.swing.table.AbstractTableModel;

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
            if (gameState.getTurnPhase() == TurnPhase.MOVING) {
            }
            int numberOfIsland = Integer.parseInt(input.getPlayerInput());
            invoker.executeAction(new MoveMotherNature(playerNickname, numberOfIsland));
          }
          case "pick cloud" -> {

          }

          case "activate cc" -> {
            invoker.executeAction(new ChooseCharacterCard(0));
            CharacterCard cc = gameState.getPlayingField().getPlayedCharacterCard();
            if(cc.requiresInput())
              (new CCMenu()).commandMenu(invoker, playerNickname, gameState);
            else
              invoker.executeAction(new ActivateCCEffect(cc));
          }
          default -> output.show("Invalid command.");
        }
        if (gameState.checkWinCondition())
          gameState.getWinner();

      }
    } while (gameState.getGamePhase() == GamePhase.ACTION);

  }

}
