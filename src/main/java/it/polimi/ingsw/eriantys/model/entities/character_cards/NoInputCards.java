package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.NoInputCC;

/**
 * Class dedicated to the Character Cards which needs no further inputs
 */
public class NoInputCards extends CharacterCard {
  private final NoInputCC noInputEffect;

  public NoInputCards(NoInputCC noInputEffect, CharacterCardEnum card) {
    super(card);
    this.noInputEffect = noInputEffect;
  }

  @Override
  public void applyEffect(GameState gameState) {
    noInputEffect.applyEffect(gameState);
    gameState.getCurrentPlayer().removeCoins(getCost());
    gameState.getPlayingField().addCoinsToBank(getCost());
    used = true;
  }

  @Override
  public boolean isValid(GameState gameState) {
    return isPurchasable(gameState.getCurrentPlayer().getCoins());
  }

}
