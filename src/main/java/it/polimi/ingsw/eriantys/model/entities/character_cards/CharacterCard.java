package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

public interface CharacterCard {
  void applyEffect(GameState gameState);
  int getCost();
  boolean requiresInput();
  boolean isValid(GameState gameState);
  CharacterCardEnum getCardEnum();
}
