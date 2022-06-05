package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class MoveStudentsToDiningHall extends GameAction {
  private final Students students;

  public MoveStudentsToDiningHall(Students students) {
    this.students = students;
  }

  /**
   * moves students from entrance to dining hall,
   *  if this is the last allowed movement it advances turn phase
   */
  @Override
  public void apply(GameState gameState) {
    Slot currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    Slot destination = gameState.getCurrentPlayer().getDashboard().getDiningHall();
    StudentsMovement move = new StudentsMovement(students, currEntrance, destination);
    GameService.placeStudents(move);

    int studentsInEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance().getCount();
    int studentsToBeLeft = gameState.getRuleBook().entranceSize - gameState.getRuleBook().playableStudentCount;

    if(studentsInEntrance <= studentsToBeLeft)
      gameState.advanceTurnPhase();
  }

  /**
   * Checks if the current player's entrance has enough students for action
   */
  @Override
  public boolean isValid(GameState gameState) {
    Students currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    for (var color : HouseColor.values()) {
      if (!currEntrance.hasEnough(color, students.getCount(color)))
        return false;
    }
    return gameState.getTurnPhase() == TurnPhase.PLACING &&
            gameState.getGamePhase() == GamePhase.ACTION;
  }
}
