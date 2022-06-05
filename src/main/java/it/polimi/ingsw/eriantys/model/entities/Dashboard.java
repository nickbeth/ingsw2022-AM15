package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class Dashboard {
  private final Students entrance;
  private final Students diningHall;
  private final Towers towers = new Towers();

  public Dashboard(Students entrance, int towerCount, TowerColor towerColor) {
    towers.color = towerColor;
    towers.count = towerCount;
    diningHall = new Students();
    this.entrance = new Students(entrance);
  }

  public Towers getTowers() {
    return towers;
  }

  public void addToEntrance(Students s) {
    entrance.addStudents(s);
  }

  public boolean isEntranceFull(RuleBook ruleBook) {
    return entrance.getCount() == ruleBook.entranceSize;
  }

  public Students getEntrance() {
    return entrance;
  }

  public Students getDiningHall() {
    return diningHall;
  }

  /**
   * Adds the given amount of tower
   *
   * @param amount The amount of towers to add
   */
  public void addTowers(int amount) {
    towers.count += amount;
  }

  /**
   * Removes the given amount of tower. If the amount is greater than towers count then it goes to 0.
   *
   * @param amount The amount of towers to remove
   */
  public void removeTowers(int amount) {
    // Could be simplified with:
    // towers.count = Max(0,towers.count - amount);
    if (towers.count < amount) {
      towers.count = 0;
      modelLogger.warn("Cannot remove any more tower");
    } else {
      towers.count -= amount;
    }
  }

  public boolean noMoreTowers() {
    return towers.count == 0;
  }

  public int towerCount() {
    return towers.count;
  }

  public TowerColor towerColor() {
    return towers.color;
  }
}

