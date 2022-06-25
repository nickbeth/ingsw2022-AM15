package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.model.enums.AssistantCard.getFullDeck;

public class Player implements Serializable {
  private final String nickname;
  private boolean connected;
  private Dashboard dashboard;
  private final ArrayList<AssistantCard> cards;
  private final TowerColor team;
  private AssistantCard chosenCard;
  private Integer coins;

  private int maxMovement;

  public Player(RuleBook ruleBook, String nickname, TowerColor color, Students entranceStudents) {
    this.nickname = nickname;
    this.team = color;
    this.coins = RuleBook.INITIAL_COINS;
    this.dashboard = new Dashboard(entranceStudents, ruleBook.dashboardTowerCount, color);
    this.cards = getFullDeck();
    this.chosenCard = null;
    this.connected = true;
  }

  public String getNickname() {
    return nickname;
  }

  public Optional<AssistantCard> getChosenCard() {
    return Optional.ofNullable(chosenCard);
  }

  public int getMaxMovement() {
    return maxMovement;
  }

  public void addToMaxMovement(int moves) {
    maxMovement += moves;
  }

  public TowerColor getColorTeam() {
    return team;
  }

  public int getTurnPriority() {
    return chosenCard != null ? chosenCard.value : 100;
  }

  public void addCoins() {
    coins++;
  }

  public void addCoins(int amount) {
    coins += amount;
  }

  public Integer getCoins() {
    return coins;
  }

  public void removeCoins(int amount) {
    if (coins - amount < 0) {
      modelLogger.warn("Not enough coin to remove");
    } else {
      coins -= amount;
    }
  }

  public Dashboard getDashboard() {
    return dashboard;
  }

  public void unsetChosenCard() {
    chosenCard = null;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  /**
   * Sets the Player's chosenCard and his maxMovement
   *
   * @param assistantCardIndex Index of the chosen card
   */
  public void setPlayedCard(int assistantCardIndex) {
    maxMovement = cards.get(assistantCardIndex).movement;
    chosenCard = cards.get(assistantCardIndex);
    cards.remove(assistantCardIndex);
  }

  public ArrayList<AssistantCard> getCards() {
    return cards;
  }

  public void setDashboard(Dashboard dashboard) {
    this.dashboard = dashboard;
  }

  @Override
  public String toString() {
    return nickname;
  }
}
