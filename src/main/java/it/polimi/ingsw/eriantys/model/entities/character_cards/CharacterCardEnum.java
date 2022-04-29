package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.*;

public enum CharacterCardEnum {
  IGNORE_COLOR
          ("", NO_INPUT, 3, true, false),
  IGNORE_TOWERS
          ("", NO_INPUT, 3, false, false),
  ADD_TO_INFLUENCE
          ("", NO_INPUT, 3, false, false),
  DROP_STUDENTS
          ("", COLOR_INPUT, 3, true, false),
  FORCE_MOTHER_NATURE_EFFECTS
          ("", ISLAND_INDEX_INPUT, 3, true, false),
  ADD_TO_MOTHER_NATURE_MOVES
          ("", NO_INPUT, 3, false, false),
  LOCK_ISLAND
          ("", ISLAND_INDEX_INPUT, 3, true, false),
  STEAL_PROFESSOR
          ("", NO_INPUT, 3, false, false),
  ;

  public enum CharacterCardType {
    NO_INPUT,
    COLOR_INPUT,
    ISLAND_INDEX_INPUT
  }

  String description;
  CharacterCardType type;
  int cost;
  boolean requiresInput;
  boolean used;

  CharacterCardEnum(String description, CharacterCardType type, int cost, boolean requiresInput, boolean used) {
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
  public boolean isBuyable(int coins) {
    return coins >= cost;
  }
}
