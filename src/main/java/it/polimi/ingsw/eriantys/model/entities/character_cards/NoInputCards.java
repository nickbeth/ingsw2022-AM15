package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.NoInputCC;

public class NoInputCards implements CharacterCard {
  private final NoInputCC noInputEffect;
  private final CharacterCardEnum card;

  public NoInputCards(NoInputCC noInputEffect, CharacterCardEnum card) {
    this.noInputEffect = noInputEffect;
    this.card = card;
  }

  @Override
  public void applyEffect(GameState gameState) {
    noInputEffect.applyEffect(gameState);
    gameState.getCurrentPlayer().removeCoins(card.getCost());
    gameState.getPlayingField().addCoinsToBank(card.getCost());
    card.used = true;
  }

  @Override
  public int getCost() {
    return card.getCost();
  }

  @Override
  public boolean requiresInput() {
    return card.isRequiredInput();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return card.isBuyable(gameState.getCurrentPlayer().getCoins());
  }

  @Override
  public CharacterCardEnum getCardEnum() {
    return card;
  }
}
