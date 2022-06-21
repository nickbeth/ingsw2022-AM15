package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;

import java.util.ArrayList;
import java.util.List;

public class RefillClouds extends GameAction {
  private final List<Students> clouds;

  public RefillClouds(GameState gameState) {
    List<Students> cloudsStudents = new ArrayList<>();
    Students temp = new Students();
    StudentBag currentBag = gameState.getPlayingField().getStudentBag();

    // Populate clouds with random students from bag
    for (int cloudIter = 0; cloudIter < gameState.getRuleBook().cloudCount; cloudIter++) {
      for (int cloudSizeIter = 0; cloudSizeIter < gameState.getRuleBook().playableStudentCount; cloudSizeIter++) {
        temp.addStudent(currentBag.takeRandomStudent());
      }
      cloudsStudents.add(new Students(temp));
      temp = new Students(); // clear temp
    }
    clouds = cloudsStudents;
  }

  /**
   * @deprecated {@link #RefillClouds(GameState)} should be used instead.
   */
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
