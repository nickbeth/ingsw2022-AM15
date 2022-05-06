package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.util.ArrayList;
import java.util.List;

public class ActionInvoker {
  protected List<GameAction> gameActions;
  protected GameState gameState;

  public ActionInvoker(GameState gameState) {
    gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  /**
   * Execute the Game Action passed as parameter
   *
   * @param action The Action which modifies the gameState
   * @return True if action is valid and succeed. False otherwise.
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
