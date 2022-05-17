package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.util.ArrayList;
import java.util.List;

public class ActionInvoker {
  protected List<GameAction> gameActions;
  protected final GameState gameState;

  public ActionInvoker(GameState gameState) {
    this.gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  /**
   * Applies the given {@link GameAction} to the game state.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if action was valid and was applied successfully, {@code false} otherwise
   */
  public boolean executeAction(GameAction action) {
    synchronized (gameState) {
      if (action.isValid(gameState)) {
        action.apply(gameState);
        gameActions.add(action);
        return true;
      }
      return false;
    }
  }
}
