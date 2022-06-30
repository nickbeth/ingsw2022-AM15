package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class MoveStudentsToIsland extends GameAction {
  private final Students students;
  private final int islandIndex;

  public MoveStudentsToIsland(Students students, int islandIndex) {
    this.students = students;
    this.islandIndex = islandIndex;
  }

  /**
   * Moves students from entrance to island,
   * if this is the last allowed movement it advances turn phase
   */
  @Override
  public void apply(GameState game) {
    // Moves students
    Slot currEntrance = game.getCurrentPlayer().getDashboard().getEntrance();
    Slot destination = game.getPlayingField().getIsland(islandIndex);
    StudentsMovement move = new StudentsMovement(students, currEntrance, destination);
    GameService.placeStudents(move);

    // Manage advance phase
    Students entrance = game.getCurrentPlayer().getDashboard().getEntrance();
    RuleBook rules = game.getRuleBook();

    description = String.format("'%s' has moved %s to island %s",
        game.getCurrentPlayer(), studentMessage(students), islandIndex);

    if (entrance.getCount() == rules.entranceSize - rules.playableStudentCount)
      game.advance();
  }

  private String studentMessage(Students students) {
    String baseMessage = Arrays.stream(HouseColor.values())
        .map(color -> new Pair<>(color, students.getCount(color)))
        .filter(pair -> !pair.getValue().equals(0))
        .map(pair -> " " + pair.getValue() + "-" + pair.getKey())
        .reduce(String::concat)
        .get();

    return students.getCount() == 1 ?
        baseMessage + " student" :
        baseMessage + " students";
  }

  /**
   * Checks if the current player's entrance has enough students for action
   */
  @Override
  public boolean isValid(GameState gameState) {
    Students currEntrance = gameState.getCurrentPlayer().getDashboard().getEntrance();
    List<Island> islands = gameState.getPlayingField().getIslands();

    if (islandIndex >= islands.size())
      return false;

    for (var color : HouseColor.values()) {
      if (!currEntrance.hasEnough(color, students.getCount(color)))
        return false;
    }

    int studentsLeftToMove = currEntrance.getCount() - (gameState.getRuleBook().entranceSize - gameState.getRuleBook().playableStudentCount);

    if (students.getCount() > studentsLeftToMove)
      return false;

    if (students.getCount() > gameState.getRuleBook().playableStudentCount)
      return false;

    return gameState.getTurnPhase() == TurnPhase.PLACING &&
        gameState.getGamePhase() == GamePhase.ACTION;
  }
}
