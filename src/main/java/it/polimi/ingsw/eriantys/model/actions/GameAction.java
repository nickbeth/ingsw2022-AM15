package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

public interface GameAction extends Serializable {
  void apply(GameState gameState);
  boolean isValid(GameState gameState);
}
