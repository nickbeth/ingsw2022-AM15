package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public class MoveStudentsToDiningHall implements GameAction {
  private final Students students;

  public MoveStudentsToDiningHall(Students students) {
    this.students = students;
  }

  @Override
  public void apply(GameState gameState, IGameService gameService) {
    Slot currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    Slot destination = gameState.getCurrentPlayer().getDashboard().getDiningHall();
    StudentsMovement move = new StudentsMovement(students, currEntrance, destination);
    gameService.placeStudent(move);
  }

  @Override
  public boolean isValid(GameState gameState) {
    Students currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    for (var color : HouseColor.values()) {
      if (!currEntrance.hasEnough(color, students.getCount(color)))
        return false;
    }
    return true;
  }
}
