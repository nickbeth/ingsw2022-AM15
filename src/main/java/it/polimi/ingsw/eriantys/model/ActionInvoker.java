package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.util.*;


public class ActionInvoker {
  private List<GameAction> gameActions;
  private GameState gameState;

  public ActionInvoker(GameState gameState) {
    gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  public boolean executeAction(GameAction action) {
    if (action.isValid(gameState)) {
      action.apply(gameState);
      gameActions.add(action);
      return true;
    }
    return false;
  }
}
