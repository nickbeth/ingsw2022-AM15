package it.polimi.ingsw.eriantys.model.entities.enums;

import java.util.ArrayList;
import java.util.List;

public class Player {
  private final String nickname;
  private ArrayList<AssistantCard> cards;
  private int maxMovement;
  private int turnPriority;
  private int coins;

  public String getNickname() {
    return nickname;
  }

  public ArrayList<AssistantCard> getCards() {
    return cards;
  }

  public int getMaxMovement() {
    return maxMovement;
  }

  public int getTurnPriority() {
    return turnPriority;
  }

  public void addCoin() {
    coins++;
  }

  public void removeCoin() {
    try {
      coins--;
    } catch () {

    }
  }
}
