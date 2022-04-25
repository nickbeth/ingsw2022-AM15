package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public interface CharacterCard {
  void applyEffect(GameState gameState, IGameService gameService);
  int getCost();
  boolean requiresInput();
  boolean isValid(GameState gameState);
}
