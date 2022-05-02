package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.ColorInputCC;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public class ColorInputCards implements CharacterCard {
  private final ColorInputCC colorInputCC;
  private final CharacterCardEnum card;
  private final HouseColor color;

  public ColorInputCards(ColorInputCC colorInputCC, CharacterCardEnum card, HouseColor color) {
    this.colorInputCC = colorInputCC;
    this.card = card;
    this.color = color;
  }

  @Override
  public void applyEffect(GameState gameState) {
    colorInputCC.applyEffect(gameState, color);
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
    return card.requiresInput;
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
