package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.INDEPENDENT;
import static it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum.CharacterCardType.INFLUENCE_MODIFIER;

public enum CharacterCardEnum {
  IGNORE_COLOR
          ("", INFLUENCE_MODIFIER, 3, true, false),
  IGNORE_TOWERS
          ("", INFLUENCE_MODIFIER, 3, false, false),
  ADD_TO_INFLUENCE
          ("", INFLUENCE_MODIFIER, 3, false, false),
  DROP_STUDENTS
          ("", INDEPENDENT, 3, true, false),
  FORCE_MOTHER_NATURE_EFFECTS
          ("", INDEPENDENT, 3, true, false),
  ADD_TO_MOTHER_NATURE_MOVES
          ("", INDEPENDENT, 3, false, false),
  LOCK_ISLAND
          ("", INDEPENDENT, 3, true, false),
  STEAL_PROFESSOR
          ("", INDEPENDENT, 3, false, false),
  ;

  public enum CharacterCardType {
    INFLUENCE_MODIFIER,
    INDEPENDENT
  }

  String description;
  CharacterCardType type;
  int cost;
  boolean requiresInput;
  boolean used;

  int islandIndex;

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
    return cost;
  }

  public boolean isRequiredInput() {
    return requiresInput;
  }

  public boolean isBuyable(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
