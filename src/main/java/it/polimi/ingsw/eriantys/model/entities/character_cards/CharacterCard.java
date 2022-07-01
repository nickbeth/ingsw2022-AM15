package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

public abstract class CharacterCard implements Serializable {
  protected final CharacterCardEnum card;
  protected boolean used = false;

  protected CharacterCard(CharacterCardEnum card) {
    this.card = card;
  }

  public abstract void applyEffect(GameState gameState);

  public abstract boolean isValid(GameState gameState);

  public boolean requiresInput() {
    return card.isRequiredInput();
  }

  /**
   * Returns the cost of the card incremented by one if the card is used
   */
  public int getCost() {
    return !used ? card.getCost() : card.getCost() + 1;
  }

  public boolean isUsed() {
    return used;
  }

  public CharacterCardEnum getCardEnum() {
    return card;
  }

  /**
   * Returns true if the given amount of coins is enough to use the card
   */
  public boolean isPurchasable(int coins) {
    return coins >= getCost();
  }
}
