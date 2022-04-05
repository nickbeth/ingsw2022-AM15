package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class PickAssistantCard extends PlayerAction {
  @Override
  public void apply(GameState gameState) {

  }

  @Override
  public boolean isValid(GameState gameState) {

    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    // check if it's PLANNING phase
    if(!gameState.getGamePhase().equals(GamePhase.PLANNING))
      return false;
    return true;
  }
}
