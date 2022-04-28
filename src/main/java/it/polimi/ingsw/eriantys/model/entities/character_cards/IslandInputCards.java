package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.IslandInputCC;

public class IslandInputCards implements CharacterCard {
  private final IslandInputCC onIslandEffect;
  private final CharacterCardEnum card;
  private final int islandIndex;

  public IslandInputCards(IslandInputCC onIslandEffect, CharacterCardEnum card, int islandIndex) {
    this.onIslandEffect = onIslandEffect;
    this.card = card;
    this.islandIndex = islandIndex;
  }

  @Override
  public void applyEffect(GameState gameState) {
    onIslandEffect.applyEffect(gameState, islandIndex);
    gameState.getCurrentPlayer().removeCoins(card.getCost());
    gameState.getPlayingField().addCoinsToBank(card.getCost());
  }

  @Override
  public int getCost() {
    return 0;
  }

  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public boolean isValid(GameState gameState) {
    return false;
  }
}
