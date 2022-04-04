package it.polimi.ingsw.eriantys.model.enums;

import java.util.ArrayList;

public enum AssistantCard {
  ONE(1, 1),
  TWO(2, 1),
  THREE(3, 2),
  FOUR(4, 2),
  FIVE(5, 3),
  SIX(6, 3),
  SEVEN(7, 4),
  EIGHT(8, 4),
  NINE(9, 5),
  TEN(10, 5);

  public final int value;
  public final int movement;

  AssistantCard(int value, int movement) {
    this.value = value;
    this.movement = movement;
  }

  public static ArrayList<AssistantCard> getFullDeck() {
    ArrayList<AssistantCard> temp = new ArrayList<>();
    temp.add(ONE);
    temp.add(TWO);
    temp.add(THREE);
    temp.add(FOUR);
    temp.add(FIVE);
    temp.add(SIX);
    temp.add(SEVEN);
    temp.add(EIGHT);
    temp.add(NINE);
    temp.add(TEN);
    return temp;
  }

  @Override
  public String toString() {
    return "AssistantCard{" +
            "value=" + value +
            ", movement=" + movement +
            '}';
  }
}
