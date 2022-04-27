package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

public interface IslandInputCCI {
  void applyEffect(GameState gameState, int islandIndex);
}
