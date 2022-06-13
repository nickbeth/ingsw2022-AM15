package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

public interface CharacterCard extends Serializable {
  void applyEffect(GameState gameState);
  int getCost();
  boolean requiresInput();
  boolean isValid(GameState gameState);
  boolean isUsed();
  CharacterCardEnum getCardEnum();
}
