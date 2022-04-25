package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;

public class AddonInfluence implements InfluenceModifierCC, CharacterCard {
  private static final Integer VALUE = 2;

  @Override
  public Integer applyModifier(Integer influence) {
    return influence + VALUE;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {

  }

  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public boolean isValid() {
    return false;
  }
}
