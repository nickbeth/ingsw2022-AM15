package it.polimi.ingsw.eriantys.model.entities;

import java.util.ArrayList;
import java.util.Optional;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

import static it.polimi.ingsw.eriantys.model.enums.AssistantCard.getFullDeck;

public class Player {
  private final String nickname;
  private boolean connected;
  private final Dashboard dashboard;
  private final ArrayList<AssistantCard> cards;
  private final TowerColor team;
  private Optional<AssistantCard> chosenCard = Optional.empty();;

  private int maxMovement;
  private int coins;

  public Player(RuleBook ruleBook, String nickname, TowerColor color, Students entranceStudents) {
    this.nickname = nickname;
    team = color;
    coins = RuleBook.INITIAL_COINS;
    dashboard = new Dashboard(entranceStudents, ruleBook.dashboardTowerCount, color);
    cards = getFullDeck();
  }

  public String getNickname() {
    return nickname;
  }

  public Optional<AssistantCard> getChosenCard() {
    return chosenCard;
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
    return chosenCard.isPresent() ? chosenCard.get().value : 0 ;
  }

  public void addCoin() {
    coins++;
  }

  public void removeCoins(int amount) {
    if (coins - amount < 0) {
      Logger.warn("Not enough coin to remove");
    } else {
      coins -= amount;
    }
  }

  public int getCoins() {
    return coins;
  }

  public Dashboard getDashboard() {
    return dashboard;
  }

  public boolean isConnected() {
    return connected;
  }

  public void unsetChosenCard() {
    chosenCard = Optional.empty();
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
    chosenCard = Optional.ofNullable(cards.get(assistantCardIndex));
    cards.remove(assistantCardIndex);
  }

  public ArrayList<AssistantCard> getCards() {
    return cards;
  }
}
