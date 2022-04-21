package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public abstract class Slot {
  public abstract void removeStudentFromSlot(HouseColor color);
  public abstract void addStudentToSlot(HouseColor color);
}
