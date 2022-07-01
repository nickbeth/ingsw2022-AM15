package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.IslandInputCC;

/**
 * Class dedicated to the Character Cards which needs islandIndex input
 */
public class IslandInputCards extends CharacterCard {
  private final IslandInputCC onIslandEffect;
  private int islandIndex = -1;

  public IslandInputCards(IslandInputCC onIslandEffect, CharacterCardEnum card) {
    super(card);
    this.onIslandEffect = onIslandEffect;
  }

  public void setIslandIndex(int islandIndex) {
    this.islandIndex = islandIndex;
  }

  @Override
  public void applyEffect(GameState gameState) {
    boolean isUsed = gameState.getPlayingField().isCharacterCardUsed(getCardEnum());
    onIslandEffect.applyEffect(gameState, islandIndex);
    gameState.getCurrentPlayer().removeCoins(getCost(isUsed));
    gameState.getPlayingField().addCoinsToBank(getCost(isUsed));
    gameState.getPlayingField().setCharacterCardAsUsed(getCardEnum());
  }

  @Override
  public boolean isValid(GameState gameState) {
    if (card == CharacterCardEnum.LOCK_ISLAND && gameState.getPlayingField().getLocks() <= 0) {
      return false;
    }
    boolean isUsed = gameState.getPlayingField().isCharacterCardUsed(getCardEnum());
    return isPurchasable(gameState.getCurrentPlayer().getCoins(), isUsed)
        && islandIndex >= 0
        && islandIndex < gameState.getPlayingField().getIslandsAmount();
  }
}
