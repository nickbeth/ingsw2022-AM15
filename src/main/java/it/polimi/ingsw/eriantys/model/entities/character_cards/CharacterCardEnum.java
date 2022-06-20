package it.polimi.ingsw.eriantys.model.entities.character_cards;

import java.io.Serializable;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.*;

public enum CharacterCardEnum implements Serializable {
  // TODO: give CC descriptions and right cost
  IGNORE_COLOR
          (false, true, 0, COLOR_INPUT,
              "DESCRIPTION"),
  IGNORE_TOWERS
          (false, false, 0, NO_INPUT,
              "DESCRIPTION"),
  ADD_TO_INFLUENCE
          (false, false, 0, NO_INPUT,
              "DESCRIPTION"),
  DROP_STUDENTS
          (false, true, 0, COLOR_INPUT,
              "DESCRIPTION"),
  FORCE_MOTHER_NATURE_EFFECTS
          (false, true, 0, ISLAND_INDEX_INPUT,
              "DESCRIPTION"),
  ADD_TO_MOTHER_NATURE_MOVES
          (false, false, 0, NO_INPUT,
              "DESCRIPTION"),
  LOCK_ISLAND
          (false, true, 0, ISLAND_INDEX_INPUT,
              "DESCRIPTION"),
  STEAL_PROFESSOR
          (false, false, 0, NO_INPUT,
              "DESCRIPTION"),
  ;

  public enum CharacterCardType {
    NO_INPUT,
    COLOR_INPUT,
    ISLAND_INDEX_INPUT
  }

  final String description;
  final CharacterCardType type;
  int cost;
  final boolean requiresInput;
  boolean used;

  CharacterCardEnum(boolean used, boolean requiresInput, int cost, CharacterCardType type, String description) {
    this.description = description;
    this.type = type;
    this.cost = cost;
    this.requiresInput = requiresInput;
    this.used = used;
  }

  public String getDescription() {
    return description;
  }

  public CharacterCardType getType() {
    return type;
  }

  public int getCost() {
    return !used ? cost : cost + 1;
  }

  public boolean isRequiredInput() {
    return requiresInput;
  }

  /**
   * returns true if the given amount of coins is enough to use the card
   */
  public boolean isPurchasable(int coins) {
    return coins >= cost;
  }
}
