package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;
import org.tinylog.Logger;

public class Dashboard {
  private Students entrance;
  private Students diningHall;
  private Towers towers = new Towers();

  public Dashboard(Students entrance, int towerCount, TowerColor towerColor) {
    towers.color = towerColor;
    towers.count = towerCount;
    diningHall = new Students();
    this.entrance = new Students(entrance);
  }

  public void addToEntrance(Students s) {
    entrance.addStudents(s);
  }

  public Students getEntrance() {
    return entrance;
  }

  public void removeFromEntrance(HouseColor color) {
    if(!entrance.tryRemoveStudent(color))
      Logger.warn("Impossible to remove student from entrance. No students available");
  }

  public Students getDiningHall() {
    return diningHall;
  }

  public void removeFromDining(HouseColor color) {
    if (!diningHall.tryRemoveStudent(color))
      Logger.warn("Impossible to remove student from diningHall. No students available");
  }

  public void addTower() {
    towers.count++;
  }

  public void removeTower() {
    if (towers.count == 0) {
      Logger.warn("No tower to remove from dashboard");
    } else {
      towers.count--;
    }
  }
}

