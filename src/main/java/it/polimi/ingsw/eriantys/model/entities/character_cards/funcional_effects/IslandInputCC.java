package it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects;

import it.polimi.ingsw.eriantys.model.GameState;

public interface IslandInputCC {
  void applyEffect(GameState gameState, int islandIndex);
}
