package it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.Serializable;

public interface ColorInputCC extends Serializable {
  void applyEffect(GameState gameState, HouseColor color);
}
