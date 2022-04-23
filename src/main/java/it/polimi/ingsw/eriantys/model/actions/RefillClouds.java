package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Students;

import java.util.List;

public class RefillClouds extends PlayerAction {
  List<Students> studentsList;

  public RefillClouds(String nickname, List<Students> s) {
    this.playerNickname = nickname;
    this.studentsList = s;
  }

  /**
   * Refills the clouds with the given students and removes them from the bag
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    gameState.getPlayingField().refillClouds(studentsList);
  }

  /**
   * Checks:
   * - if the list of given students is the right size
   * - if the given students are the right amount
   * @param gameState
   * @return
   */
  @Override
  public boolean isValid(GameState gameState) {
    for (var s : studentsList)
      if (s.getCount() != gameState.getRuleBook().playableStudentCount)
        return false;
    return studentsList.size() == gameState.getRuleBook().cloudCount;
  }
}
