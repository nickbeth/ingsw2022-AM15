package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.ArrayList;
import java.util.List;

public class DropStudents implements GameAction {
  private HouseColor studentColor;
  private String playerNickname;

  public DropStudents(String nickname, HouseColor color) {
    this.studentColor = color;
    this.playerNickname = nickname;
  }

  /**
   * removes 3 students of a certain HouseColor from the entrance of each player
   * then advances to next TurnPhase;
   *
   * @param gameState
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    final int DROP_STUDENTS_AMOUNT = 3;
    List<Students> diningHallList = new ArrayList<>();
    gameState.getPlayers().forEach((player) ->
            diningHallList.add(player.getDashboard().getDiningHall())
    );
    StudentBag bag = gameState.getPlayingField().getStudentBag();
    gameService.dropStudents(diningHallList, studentColor, DROP_STUDENTS_AMOUNT, bag);

    gameState.advanceTurnPhase();
  }

  /**
   * checks:<br/>
   * - If current player is the player who did the action<br/>
   * - If the gamePhase is ACTION<br/>
   * - If the turnPhase is EFFECT<br/>
   *
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.EFFECT;
  }
}
