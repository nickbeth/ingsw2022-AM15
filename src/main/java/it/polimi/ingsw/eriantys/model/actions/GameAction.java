package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

public interface GameAction {
  void apply(GameState gameState);
  boolean isValid(GameState gameState);
}
