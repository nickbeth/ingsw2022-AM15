package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ICardEffectsService;

public interface InfluenceModifierCC {
  void applyModifier(GameState gameState, ICardEffectsService characterCardEffects);
}
