package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.StudentSlot;

/**
 * Record which trace the data for moving students
 */
public class StudentMovement {
  public HouseColor studentColor;
  public StudentSlot src;
  public StudentSlot dest;
  public int islandIndex;
}