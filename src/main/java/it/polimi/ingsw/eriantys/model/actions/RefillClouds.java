package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;

import java.util.List;

public class RefillClouds implements GameAction {
  private final List<Students> clouds;

  public RefillClouds(List<Students> s) {
    this.clouds = s;
  }

  /**
   * calls gameService that fills the clouds with the given students and removes them from the bag
   */
  @Override
  public void apply(GameState gameState) {
    StudentBag studentBag = gameState.getPlayingField().getStudentBag();
    GameService.refillClouds(studentBag, gameState.getPlayingField().getClouds(), clouds);
  }

  /**
   * Checks: <br/>
   * - if the list of given students is the right size <br/>
   * - if the given students are the right amount
   */
  @Override
  public boolean isValid(GameState gameState) {
    for (var students : clouds)
      if (students.getCount() != gameState.getRuleBook().playableStudentCount)
        return false;
    return clouds.size() == gameState.getRuleBook().cloudCount;
  }
}
