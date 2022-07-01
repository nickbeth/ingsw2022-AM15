package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

public abstract class CharacterCard implements Serializable {
  protected final CharacterCardEnum card;
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
  public int getCost(boolean isUsed) {
    return !isUsed ? card.getCost() : card.getCost() + 1;
  }

  public CharacterCardEnum getCardEnum() {
    return card;
  }

  /**
   * Returns true if the given amount of coins is enough to use the card
   */
  public boolean isPurchasable(int coins, boolean isUsed) {
    return coins >= getCost(isUsed);
  }
}
