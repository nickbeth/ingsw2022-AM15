package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.GameAction;

import java.util.*;

import static it.polimi.ingsw.eriantys.model.GameService.getGameService;

public class ActionInvoker {
  private List<GameAction> gameActions;
  private GameState gameState;
  private IGameService gameService;

  private ActionInvoker(GameState gameState) {
    gameService = getGameService();
    gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  public boolean executeAction(GameAction action) {
    if (action.isValid(gameState)) {
      action.apply(gameState, gameService);
      gameActions.add(action);
      return true;
    }
    return false;
  }
}
