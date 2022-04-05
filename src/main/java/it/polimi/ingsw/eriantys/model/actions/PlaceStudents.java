package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.StudentSlot;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static it.polimi.ingsw.eriantys.model.enums.StudentSlot.ENTRANCE;

public class PlaceStudents extends PlayerAction {
  private final List<StudentMovement> entries;

  public PlaceStudents(String nickname, List<StudentMovement> entries) {
    this.playerNickname = nickname;
    this.entries = entries;
  }

  /**
   * Executes the movements: <br/>
   * - removes given students from src <br/>
   * - adds given students to dest
   * @param gameState GameState
   */
  @Override
  public void apply(GameState gameState) {
    // For each moves
    for (StudentMovement move : entries) {
      // Remove the student from the source
      switch (move.src()) {
        case ENTRANCE -> gameState.
                getCurrentPlayer().getDashboard().getEntrance().tryRemoveStudent(move.studentColor());
        case DINIGN -> gameState.
                getCurrentPlayer().getDashboard().getDiningHall().tryRemoveStudent(move.studentColor());
        default -> throw new IllegalStateException("Unexpected value: " + move.src());
      }

      // Add the student from the source
      switch (move.dest()) {
        case ENTRANCE -> gameState.
                getCurrentPlayer().getDashboard().getEntrance().addStudent(move.studentColor());
        case DINIGN -> gameState.
                getCurrentPlayer().getDashboard().getDiningHall().addStudent(move.studentColor());
        case ISLAND -> gameState.
                getPlayingField().getIsland(move.islandIndex()).getStudents().addStudent(move.studentColor());
        default -> throw new IllegalStateException("Unexpected value: " + move.src());
      }
    }
  }

  /**
   * Checks the following condition:<br/>
   * - if it's ACTION phase,<br/>
   * - if it's PLACING phase, <br/>
   * - if the client calling the action is the current player <br/>
   * - if the game has all the students the player wants to move <br/>
   *
   * @param gameState GameState
   * @return True if the action is doable. False otherwise
   */
  @Override
  public boolean isValid(GameState gameState) {
    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    if (!(gameState.getTurnPhase() == TurnPhase.PLACING)) return false;
    if (!(gameState.getGamePhase() == GamePhase.ACTION)) return false;

    // Counts how many students are wanted to be moved
    EnumMap<StudentSlot, Students> wantedStudents = new EnumMap<>(StudentSlot.class);
    for (var slot : StudentSlot.values())
      wantedStudents.put(slot, new Students());
    entries.stream().forEach((move) -> wantedStudents.get(move.src()).addStudent(move.studentColor()));

    // Checks if there are enough students to be moved
    Dashboard gameDashboard = gameState.getCurrentPlayer().getDashboard();
    for (HouseColor color : HouseColor.values()) {
      Logger.debug("Entrance Color: {} Count: {} Wanted: {}"
              , color, gameDashboard.getEntrance().getCount(color), wantedStudents.get(ENTRANCE).getCount(color));
      Logger.debug("Dining Color: {} Count: {} Wanted: {}"
              , color, gameDashboard.getDiningHall().getCount(color), wantedStudents.get(StudentSlot.DINIGN).getCount(color));
      if (gameDashboard.getEntrance().getCount(color) < wantedStudents.get(ENTRANCE).getCount(color)) {
        return false;
      }
      if (gameDashboard.getDiningHall().getCount(color) < wantedStudents.get(StudentSlot.DINIGN).getCount(color)) {
        return false;
      }
    }
    return true;
  }
}

