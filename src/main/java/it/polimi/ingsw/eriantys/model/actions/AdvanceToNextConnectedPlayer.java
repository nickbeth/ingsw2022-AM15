package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

public class AdvanceToNextConnectedPlayer extends GameAction {
  @Override
  public void apply(GameState gameState) {
    gameState.advancePlayer();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
