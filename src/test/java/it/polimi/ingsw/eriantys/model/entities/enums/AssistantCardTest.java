package it.polimi.ingsw.eriantys.model.entities.enums;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

  @Test
  void getFullDeck() {
    ArrayList<AssistantCard> cards = AssistantCard.getFullDeck();
    for (AssistantCard c : cards) {
      System.out.println(c);
    }
  }
}