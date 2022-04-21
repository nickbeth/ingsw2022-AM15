package it.polimi.ingsw.eriantys.model;

import java.util.*;

public class ActionInvoker {
  private List<PlayerAction> gameActions;
  private GameState gameState;
  private IGameService gameService;

  private ActionInvoker(GameState gameState) {
    gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  public boolean executeAction(PlayerAction action) {
    if (action.isValid(gameState)) {
      action.apply(gameState,gameService);
      gameActions.add(action);
      return true;
    }
    return false;
  }
}
