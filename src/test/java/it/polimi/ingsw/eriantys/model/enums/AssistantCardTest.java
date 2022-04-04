package it.polimi.ingsw.eriantys.model.entities.enums;

import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class AssistantCardTest {

  @Test
  void getFullDeck() {
    ArrayList<AssistantCard> cards = AssistantCard.getFullDeck();
    for (AssistantCard c : cards) {
      System.out.println(c);
    }
  }
}