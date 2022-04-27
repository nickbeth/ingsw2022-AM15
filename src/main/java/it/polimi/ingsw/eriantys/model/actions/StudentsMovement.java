package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;

public record StudentsMovement(Students students,
                               Slot src,
                               Slot dest) {
}
