package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.IslandInputCC;

/**
 * Class dedicated to the Character Cards which needs islandIndex input
 */
public class IslandInputCards implements CharacterCard {
  private final IslandInputCC onIslandEffect;
  private final CharacterCardEnum card;
  private int islandIndex = -1;

  public IslandInputCards(IslandInputCC onIslandEffect, CharacterCardEnum card) {
    this.onIslandEffect = onIslandEffect;
    this.card = card;
  }

  public void setIslandIndex(int islandIndex) {
    this.islandIndex = islandIndex;
  }

  @Override
  public void applyEffect(GameState gameState) {
    onIslandEffect.applyEffect(gameState, islandIndex);
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
    if (card == CharacterCardEnum.LOCK_ISLAND && gameState.getPlayingField().getLocks() <= 0) {
      return false;
    }
    return card.isPurchasable(gameState.getCurrentPlayer().getCoins())
        && islandIndex >= 0
        && islandIndex < gameState.getPlayingField().getIslandsAmount();
  }

  @Override
  public boolean isUsed() {
    return card.used;
  }

  @Override
  public CharacterCardEnum getCardEnum() {
    return card;
  }
}
