package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

public class GoToNextRound extends GameAction {
  @Override
  public void apply(GameState gameState) {
    gameState.advanceTurnPhase();
    if (gameState.isLastPlayer(gameState.getCurrentPlayer())) {
      gameState.advanceGamePhase();
    }
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
