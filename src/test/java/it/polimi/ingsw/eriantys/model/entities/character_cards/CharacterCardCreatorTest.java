package it.polimi.ingsw.eriantys.model.entities.character_cards;

import org.junit.jupiter.api.Test;

public class CharacterCardCreatorTest {
  @Test
  public void characterCardsCreator() {
    for (var card : CharacterCardEnum.values()) {
      CharacterCardCreator.create(card);
    }
  }
}