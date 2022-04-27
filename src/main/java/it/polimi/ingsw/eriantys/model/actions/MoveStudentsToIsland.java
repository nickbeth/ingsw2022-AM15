package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

public class MoveStudentsToIsland implements GameAction {
  private Students students;
  private int islandIndex;

  public MoveStudentsToIsland(Students students, int islandIndex) {
    this.students = students;
    this.islandIndex = islandIndex;
  }

  @Override
  public void apply(GameState gameState) {
    Slot currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    Slot destination = gameState.getPlayingField().getIsland(islandIndex);
    StudentsMovement move = new StudentsMovement(students, currEntrance, destination);
    GameService.placeStudents(move);
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
