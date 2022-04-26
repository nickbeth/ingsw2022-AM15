package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public interface GameAction {
  void apply(GameState gameState, IGameService gameService);
  boolean isValid(GameState gameState);
}
