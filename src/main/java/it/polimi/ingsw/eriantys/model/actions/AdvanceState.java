package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

public class AdvanceState extends GameAction {
  @Override
  public void apply(GameState gameState) {
    gameState.advance();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
