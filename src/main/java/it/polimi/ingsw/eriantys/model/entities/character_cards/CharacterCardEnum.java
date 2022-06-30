package it.polimi.ingsw.eriantys.model.entities.character_cards;

import java.io.Serializable;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.*;

public enum CharacterCardEnum implements Serializable {
  IGNORE_COLOR
      (false, true, 0, COLOR_INPUT,
          "Choose a color of Student: during the influence calculation this turn, that color adds no influence."),
  IGNORE_TOWERS
      (false, false, 0, NO_INPUT,
          "When resolving a Conquering on an island, Towers do not count towards influence."),
  ADD_TO_INFLUENCE
      (false, false, 0, NO_INPUT,
          "During the influence calculation this turn, you count as having 2 more influence."),
  DROP_STUDENTS
      (false, true, 0, COLOR_INPUT,
          "Choose a type of Student: every player (including yourself) must return 3 Students " +
              "of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, " +
              "return as many Students as they have."),
  FORCE_MOTHER_NATURE_EFFECTS
      (false, true, 0, ISLAND_INDEX_INPUT,
          "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. " +
              "Mother Nature will still move and the Island where she ends her movement will also be resolved."),
  ADD_TO_MOTHER_NATURE_MOVES
      (false, false, 0, NO_INPUT,
          "You may move Mother Nature up to 2 additional Islands more than as " +
              "indicated by the Assistant card you've played"),
  LOCK_ISLAND
      (false, true, 0, ISLAND_INDEX_INPUT,
          "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, " +
              "put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers."),
  STEAL_PROFESSOR
      (false, false, 0, NO_INPUT,
          "During this turn, you take control of any number of Professors " +
              "even if you have the same number of Students as the player who currently controls them."),
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
