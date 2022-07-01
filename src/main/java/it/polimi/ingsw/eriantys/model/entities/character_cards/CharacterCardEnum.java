package it.polimi.ingsw.eriantys.model.entities.character_cards;

import java.io.Serializable;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.*;

public enum CharacterCardEnum implements Serializable {
  IGNORE_COLOR
      (true, 3, COLOR_INPUT,
          "Choose a color of Student: during the influence calculation this turn, that color adds no influence."),
  IGNORE_TOWERS
      (false, 3, NO_INPUT,
          "When resolving a Conquering on an island, Towers do not count towards influence."),
  ADD_TO_INFLUENCE
      ( false, 2, NO_INPUT,
          "During the influence calculation this turn, you count as having 2 more influence."),
  DROP_STUDENTS
      (true, 3, COLOR_INPUT,
          "Choose a type of Student: every player (including yourself) must return 3 Students " +
              "of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, " +
              "return as many Students as they have."),
  FORCE_MOTHER_NATURE_EFFECTS
      (true, 3, ISLAND_INDEX_INPUT,
          "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. " +
              "Mother Nature will still move and the Island where she ends her movement will also be resolved."),
  ADD_TO_MOTHER_NATURE_MOVES
      (false, 1, NO_INPUT,
          "You may move Mother Nature up to 2 additional Islands more than as " +
              "indicated by the Assistant card you've played"),
  LOCK_ISLAND
      ( true, 2, ISLAND_INDEX_INPUT,
          "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, " +
              "put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers."),
  STEAL_PROFESSOR
      (false, 2, NO_INPUT,
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
  final int cost;
  final boolean requiresInput;

  CharacterCardEnum(boolean requiresInput, int cost, CharacterCardType type, String description) {
    this.description = description;
    this.type = type;
    this.cost = cost;
    this.requiresInput = requiresInput;
  }

  public String getDescription() {
    return description;
  }

  public CharacterCardType getType() {
    return type;
  }

  public int getCost() {
    return cost;
  }

  public boolean isRequiredInput() {
    return requiresInput;
  }

}
