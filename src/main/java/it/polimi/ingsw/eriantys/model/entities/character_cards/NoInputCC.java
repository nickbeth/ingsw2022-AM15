package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

public class NoInputCC implements CharacterCard {
  private final INoInputCC noInputEffect;
  private final CharacterCardEnum card;

  public NoInputCC(INoInputCC noInputEffect, CharacterCardEnum card) {
    this.noInputEffect = noInputEffect;
    this.card = card;
  }

  @Override
  public void applyEffect(GameState gameState) {
    noInputEffect.applyEffect(gameState);
    gameState.getCurrentPlayer().removeCoins(card.getCost());
    gameState.getPlayingField().addCoinsToBank(card.getCost());
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
}
