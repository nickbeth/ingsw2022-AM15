package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;

import java.util.List;

public class RefillClouds implements GameAction {
  private List<Students> cloudStudentsList;

  public RefillClouds(List<Students> s) {
    this.cloudStudentsList = s;
  }

  /**
   * Refills the clouds with the given students and removes them from the bag
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    StudentBag studentBag = gameState.getPlayingField().getStudentBag();
    gameService.refillClouds(studentBag, gameState.getPlayingField().getClouds(), cloudStudentsList);
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
    for (var s : cloudStudentsList)
      if (s.getCount() != gameState.getRuleBook().playableStudentCount)
        return false;
    return cloudStudentsList.size() == gameState.getRuleBook().cloudCount;
  }
}
