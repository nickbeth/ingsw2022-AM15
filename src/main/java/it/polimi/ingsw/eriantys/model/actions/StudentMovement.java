package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public record StudentMovement(HouseColor studentColor,
                              Slot src,
                              Slot dest) {
}
