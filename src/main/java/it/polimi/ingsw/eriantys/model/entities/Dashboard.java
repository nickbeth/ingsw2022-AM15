package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;

import java.util.EnumMap;


public class Dashboard {
  private EnumMap<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
  private EnumMap<HouseColor, Integer> diningHall = new EnumMap<>(HouseColor.class);
  private Towers towers;

  public Dashboard(EnumMap<HouseColor, Integer> entrance, int towerCount, TowerColor towerColor) {
    towers.color = towerColor;
    towers.count = towerCount;
    for (HouseColor color : HouseColor.values()) {
      this.entrance.put(color, entrance.get(color));
      diningHall.put(color, 0);
    }
  }

  public void addToEntrance(EnumMap<HouseColor, Integer> s) {
    for (HouseColor color : HouseColor.values()) {
      entrance.put(color, Math.max(entrance.get(color) + s.get(color), 0));
    }
  }

  public EnumMap<HouseColor, Integer> getEntrance() {
    return entrance;
  }

  public boolean removeFromEntrance(HouseColor color) {
    if (entrance.get(color) == 0) {
      return false;
    }
    entrance.put(color, entrance.get(color) - 1);
    return true;
  }

  public EnumMap<HouseColor, Integer> getDiningHall() {
    return diningHall;
  }

  public boolean removeFromDining(HouseColor color) {
    if (diningHall.get(color) == 0) {
      return false;
    }
    diningHall.put(color, diningHall.get(color) - 1);
    return true;
  }

  public void addTower() {
    towers.count++;
  }

  public boolean removeTower() {
    if (towers.count == 0) {
      return false;
    }
    towers.count--;
    return true;
  }
}

