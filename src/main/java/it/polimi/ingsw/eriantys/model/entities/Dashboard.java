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

  public void removeFromEntrance(HouseColor color) {
    entrance.put(color, entrance.get(color) - 1);
  }

  public void addToDining(EnumMap<HouseColor, Integer> s) {
    for (HouseColor color : HouseColor.values()) {
      diningHall.put(color, Math.max(entrance.get(color) + s.get(color), 0));
    }
  }

  public void removeFromDining(HouseColor color) {
    diningHall.put(color, diningHall.get(color) - 1);
  }

  public void addTower() {
    towers.count++;
  }

  public void removeTower() {
    towers.count--;
  }
}
