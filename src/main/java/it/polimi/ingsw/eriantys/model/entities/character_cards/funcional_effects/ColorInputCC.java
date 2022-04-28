package it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public interface ColorInputCC {
  void applyEffect(GameState gameState, HouseColor color);
}
