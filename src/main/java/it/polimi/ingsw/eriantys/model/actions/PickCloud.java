package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class PickCloud extends GameAction {
  private List<Students> clouds;
  private final int cloudIndex;

  private boolean shouldRefill;

  public PickCloud(GameState gameState, int index) {
    this.cloudIndex = index;

    shouldRefill = gameState.isLastPlayer(gameState.getCurrentPlayer());
    if (shouldRefill) {
      List<Students> cloudsStudents = new ArrayList<>();
      Students temp = new Students();
      StudentBag currentBag = new StudentBag(gameState.getPlayingField().getStudentBag());

      // Populate clouds with random students from bag
      for (int cloudIter = 0; cloudIter < gameState.getRuleBook().cloudCount; cloudIter++) {
        for (int cloudSizeIter = 0; cloudSizeIter < gameState.getRuleBook().playableStudentCount; cloudSizeIter++) {
          temp.addStudent(currentBag.takeRandomStudent());
        }
        cloudsStudents.add(new Students(temp));
        temp = new Students(); // clear temp
      }
      this.clouds = cloudsStudents;
    }
  }

  /**
   * Gets students from pickedCloud and puts them onto the players entrance. <br>
   * If player is the last connected one refill clouds.<br>
   * Calls advance().
   */
  @Override
  public void apply(GameState game) {
    Cloud cloud = game.getPlayingField().getCloud(cloudIndex);
    Dashboard dashboard = game.getCurrentPlayer().getDashboard();

    // Checks if the player needs to pick a cloud. This is needed in case of in turn disconnection
    if (cloud.getStudents().getCount() + dashboard.getEntrance().getCount() == game.getRuleBook().entranceSize) {
      GameService.pickCloud(cloud, dashboard);
      description = String.format("'%s' picked %s cloud",
          game.getCurrentPlayer(), cloudIndex);
    }

    shouldRefill = game.isLastPlayer(game.getCurrentPlayer());
    if (shouldRefill) {
      RuleBook rules = game.getRuleBook();
      List<Cloud> gameClouds = game.getPlayingField().getClouds();

      // Eventually refill disconnected player entrances
      List<Player> players = game.getPlayers();
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

      StudentBag studentBag = game.getPlayingField().getStudentBag();

      // Refill clouds
      GameService.refillClouds(studentBag, gameClouds, clouds);

      description += "\nClouds refilled";
      game.getPlayers().forEach(Player::unsetAssistantChosenCard);
    }
    game.advance();
  }

  // Returns the first non-empty cloud between the given
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
   * @return False if:<br>
   * - the cloud index is not in range<br>
   * - the picked cloud is empty<br>
   * - the phase is ACTION and the turn is PICKING
   */
  @Override
  public boolean isValid(GameState gameState) {
    return cloudIndex >= 0 &&
        cloudIndex < gameState.getRuleBook().cloudCount &&
        (!gameState.getPlayingField().getCloud(cloudIndex).isEmpty()) &&
        gameState.getTurnPhase() == TurnPhase.PICKING &&
        gameState.getGamePhase() == GamePhase.ACTION;
  }
}
