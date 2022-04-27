package it.polimi.ingsw.eriantys.model.entities;

public abstract class Slot {
  public abstract boolean removeStudentsFromSlot(Students students);
  public abstract void addStudentsToSlot(Students students);
}
