package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

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
    RuleBook rules = gameState.getRuleBook();
    List<Cloud> gameClouds = gameState.getPlayingField().getClouds();

    // fill disconnected player entrances
    List<Player> players = gameState.getPlayers();
    players.stream()
        .map(p -> p.getDashboard().getEntrance())
        .forEach(entrance -> {
          int missingStudents = rules.entranceSize - entrance.getCount();
          // If the player has black spots in his entrance
          if (missingStudents > 0) {
            // Refill his entrance
            entrance.addStudents(pickStudentsFromCloud(Objects.requireNonNull(firstNonEmptyCloud(gameClouds)), missingStudents));
          }
        });

    StudentBag studentBag = gameState.getPlayingField().getStudentBag();

    // Refill clouds
    GameService.refillClouds(studentBag, gameClouds, clouds);
  }

  private Cloud firstNonEmptyCloud(List<Cloud> gameClouds) {
    for (var cloud : gameClouds) {
      if (!cloud.isEmpty()) return cloud;
    }
    modelLogger.error("Error implementing fix entrances");
    return null;
  }

  private Students pickStudentsFromCloud(Cloud cloud, int amount) {
    Students temp = new Students();
    Students cloudStudents = cloud.getStudents();

    for (int i = 0; i < amount; i++) {
      for (var color : HouseColor.values()) {
        if (cloudStudents.hasEnough(color, 1)) {
          temp.addStudent(color);
          break;
        }
      }
    }

    return temp;
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
