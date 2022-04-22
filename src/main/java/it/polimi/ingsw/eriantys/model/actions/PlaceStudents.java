package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Slot;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class PlaceStudents extends PlayerAction {
  private final List<StudentMovement> movements;

  public PlaceStudents(String nickname, List<StudentMovement> entries) {
    this.playerNickname = nickname;
    this.movements = entries;
  }

  /**
   * Executes the movements: <br/>
   * - removes given students from src <br/>
   * - adds given students to dest
   *
   * @param gameState   GameState
   * @param gameService
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    gameService.placeStudents(movements);
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
  // todo Da rifare il test, l'implementazione è cambiata
  public boolean isValid(GameState gameState) {
    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    if (!(gameState.getTurnPhase() == TurnPhase.PLACING)) return false;
    if (!(gameState.getGamePhase() == GamePhase.ACTION)) return false;

    // Counts how many students are wanted to be moved
    Map<Slot, Students> slotStudentsMap = new HashMap<>();
    movements.forEach((move) ->
            slotStudentsMap.put(move.src(), new Students())
    );
    // Count how many students src slot requires
    movements.forEach((move) -> {
      slotStudentsMap.get(move.src()).addStudent(move.studentColor());
    });

    AtomicBoolean isValid = new AtomicBoolean(true);
    // For each color
    for (HouseColor color : HouseColor.values()) {
      //  If there is at least one source slot which does not have enough students of
      //  that color, then it's not a valid operation
      slotStudentsMap.forEach((src,students) -> {
        ((Students)src).hasEnough(color, students.getCount(color));
        isValid.set(false);
      });
    }

    return isValid.get();
  }
}

