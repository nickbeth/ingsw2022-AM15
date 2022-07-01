package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.ColorInputCC;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

/**
 * Class dedicated to the Character Cards which needs color input
 */
public class ColorInputCards extends CharacterCard {
  private final ColorInputCC colorInputCC;
  private HouseColor color = null;

  public ColorInputCards(ColorInputCC colorInputCC, CharacterCardEnum card) {
    super(card);
    this.colorInputCC = colorInputCC;
  }

  public void setColor(HouseColor color) {
    this.color = color;
  }

  @Override
  public void applyEffect(GameState gameState) {
    boolean isUsed = gameState.getPlayingField().isCharacterCardUsed(getCardEnum());
    colorInputCC.applyEffect(gameState, color);
    gameState.getCurrentPlayer().removeCoins(getCost(isUsed));
    gameState.getPlayingField().addCoinsToBank(getCost(isUsed));
    gameState.getPlayingField().setCharacterCardAsUsed(getCardEnum());
  }

  @Override
  public boolean isValid(GameState gameState) {
    boolean isUsed = gameState.getPlayingField().isCharacterCardUsed(getCardEnum());
    return isPurchasable(gameState.getCurrentPlayer().getCoins(), isUsed);
  }
}
