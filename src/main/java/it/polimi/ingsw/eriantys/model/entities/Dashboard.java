package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.EnumMap;


public class Dashboard {
  private EnumMap<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
  private EnumMap<HouseColor, Integer> diningHall = new EnumMap<>(HouseColor.class);
  private Towers towers;

  public Dashboard(EnumMap<HouseColor, Integer> entrance, int towerCount, TowerColor towerColor) {
    towers.color = towerColor;
    towers.count = towerCount;
    Arrays.stream(HouseColor.values()).forEach(color -> {
              this.entrance.put(color, entrance.get(color));
              diningHall.put(color, 0);
            }
    );
  }

  public void addToEntrance(EnumMap<HouseColor, Integer> s) {
    entrance.forEach((color, value) -> {
      if (entrance.get(color) == 0) Logger.warn("In addToEntrance() No students");
      else entrance.put(color, value + s.get(color));
    });
  }

  public EnumMap<HouseColor, Integer> getEntrance() {
    return entrance;
  }

  public void removeFromEntrance(HouseColor color) {
    if (entrance.get(color) == 0) {
      Logger.warn("No students to remove from Entrance");
    } else {
      entrance.put(color, entrance.get(color) - 1);
    }
  }

  public EnumMap<HouseColor, Integer> getDiningHall() {
    return diningHall;
  }

  public void removeFromDining(HouseColor color) {
    if (diningHall.get(color) == 0) {
      Logger.warn("No students to remove from Dining");
    } else {
      diningHall.put(color, diningHall.get(color) - 1);
    }
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

