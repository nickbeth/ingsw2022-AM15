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
  private final ArrayList<AssistantCard> cards;
  private final Dashboard dashboard;
  private final TowerColor team;

  private Optional<AssistantCard> chosenCard;

  private int maxMovement;
  private int coins;
  public Player(RuleBook ruleBook, String nickname, TowerColor color, Students entranceStudents) {
    this.nickname = nickname;
    team = color;
    coins = RuleBook.INITIAL_COINS;
    // TODO gestire il caso di 4 players che condividono lo stesso numero di tower.
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
    return chosenCard.get().movement;
  }

  public void addToMaxMovement(int moves) {
    maxMovement += moves;
  }

  public TowerColor getColorTeam() {
    return team;
  }

  public int getTurnPriority() {
    return chosenCard.get().value;
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
    maxMovement = cards.get(assistantCardIndex).movement;
    cards.remove(assistantCardIndex);
  }

  public ArrayList<AssistantCard> getCards() {
    return cards;
  }
}
