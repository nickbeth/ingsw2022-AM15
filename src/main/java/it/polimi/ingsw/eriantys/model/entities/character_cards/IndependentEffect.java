package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public class IndependentEffect implements CharacterCard {
  private final IndependentEffectCC effect;
  private final CharacterCardEnum card;
  private HouseColor colorToBeDropped = null;

  public IndependentEffect(IndependentEffectCC effect, CharacterCardEnum card, HouseColor colorToBeDropped) {
    this.effect = effect;
    this.card = card;
    this.colorToBeDropped = colorToBeDropped;
  }

  public IndependentEffect(IndependentEffectCC effect, CharacterCardEnum card) {
    this.effect = effect;
    this.card = card;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    effect.executeEffect(gameState);
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
