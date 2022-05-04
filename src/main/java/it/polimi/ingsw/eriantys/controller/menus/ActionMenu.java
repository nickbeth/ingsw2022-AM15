package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.ActivateCCEffect;
import it.polimi.ingsw.eriantys.model.actions.ChooseCharacterCard;
import it.polimi.ingsw.eriantys.model.actions.MoveMotherNature;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class ActionMenu implements Menu {
  @Override
  public void commandMenu(ActionInvoker invoker, String playerNickname, GameState gameState, String action) {
    do {
      if (!gameState.isTurnOf(playerNickname)) {
//        show("Not your turn dumbass");
      } else {
        switch (action) {
          case "place students" -> {
          }
          case "move mother nature" -> {
            if (gameState.getTurnPhase() == TurnPhase.MOVING) {
            }
            int numberOfIsland = 0;
            invoker.executeAction(new MoveMotherNature(playerNickname, numberOfIsland));
          }
          case "pick cloud" -> {

          }

          case "activate cc" -> {
            invoker.executeAction(new ChooseCharacterCard(0));
            CharacterCard cc = gameState.getPlayingField().getPlayedCharacterCard();
            if(cc.requiresInput())
              (new CCMenu()).commandMenu(invoker, playerNickname, gameState, action);
            else
              invoker.executeAction(new ActivateCCEffect(cc));
          }
          default -> { // show("Invalid command.");
          }
        }
        if (gameState.checkWinCondition())
          gameState.getWinner();

      }
    } while (gameState.getGamePhase() == GamePhase.ACTION);

  }

}
