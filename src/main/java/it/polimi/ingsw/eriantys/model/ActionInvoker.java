package it.polimi.ingsw.eriantys.model;

import java.util.*;

import static it.polimi.ingsw.eriantys.model.GameService.getGameService;

public class ActionInvoker {
  private List<PlayerAction> gameActions;
  private GameState gameState;
  private IGameService gameService;

  private ActionInvoker(GameState gameState) {
    gameService = getGameService();
    gameActions = new ArrayList<>();
    this.gameState = gameState;
  }

  public boolean executeAction(PlayerAction action) {
    if (action.isValid(gameState)) {
      action.apply(gameState, gameService);
      gameActions.add(action);
      return true;
    }
    return false;
  }
}
