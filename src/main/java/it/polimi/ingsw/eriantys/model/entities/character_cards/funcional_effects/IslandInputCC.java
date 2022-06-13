package it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

public interface IslandInputCC extends Serializable {
  void applyEffect(GameState gameState, int islandIndex);
}
