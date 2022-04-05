package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.StudentSlot;

/**
 * Record which trace the data for moving students
 */
public record StudentMovement(HouseColor studentColor,
                              StudentSlot src,
                              StudentSlot dest, int islandIndex) {
}
