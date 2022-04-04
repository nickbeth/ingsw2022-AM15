package it.polimi.ingsw.eriantys.model.entities;

import java.util.ArrayList;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

import static it.polimi.ingsw.eriantys.model.enums.AssistantCard.getFullDeck;

public class Player {
  private final String nickname;
  private final ArrayList<AssistantCard> cards;
  private final Dashboard dashboard;
  private int maxMovement;
  private int turnPriority;
  private int coins;

  public Player(RuleBook ruleBook, String nickname, TowerColor color, Students entranceStudents) {
    this.nickname = nickname;
    coins = RuleBook.INITIAL_COINS;
    // TODO gestire il caso di 4 players che condividono lo stesso numero di tower.
    dashboard = new Dashboard(entranceStudents, ruleBook.dashboardTowerCount, color);
    cards = getFullDeck();
  }

  public String getNickname() {
    return nickname;
  }

  public int getMaxMovement() {
    return maxMovement;
  }

  public void addToMaxMovement(int moves) {
    maxMovement += moves;
  }

  public int getTurnPriority() {
    return turnPriority;
  }

  public void addCoin() {
    coins++;
  }

  public void removeCoin() {
    if (coins == 0) {
      Logger.warn("No coin to remove");
    } else {
      coins--;
    }
  }

  public Dashboard getDashboard() {
    return dashboard;
  }

  public void setPlayedCard(int assistantCardIndex) {
    turnPriority = cards.get(assistantCardIndex).movement;
    cards.remove(assistantCardIndex);
  }
}
