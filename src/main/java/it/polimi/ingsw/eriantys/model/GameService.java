package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentsMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;


public interface GameService {
  /**
   * Reallocate professors to the player who has more students in his dining
   *
   * @param professors Needs professorHolder of the game
   * @param dashboards Dashboard list of the game
   */
  static void updateProfessors(ProfessorHolder professors, List<Dashboard> dashboards) {
    professors.updateProfessors(dashboards);
  }

  /**
   * Updates Player coin amount depending on the new amount of students of each color in the dining hall
   *
   * @param students Needs the addon students
   * @param player   Player that will get his coins updated
   */
  static void updatePlayerCoins(Students students, Player player) {
    for (HouseColor color : HouseColor.values()) {
      int coinSlotAmount = 3;
      int hallStudentCount = player.getDashboard().getDiningHall().getCount(color);
      int addonStudentCount = students.getCount(color);

      if (addonStudentCount != 0) {
        int freeCoinSlotAmount = coinSlotAmount - Math.floorDiv(hallStudentCount, coinSlotAmount);
        int newFreeCoinSlotAmount = coinSlotAmount - Math.floorDiv(hallStudentCount + addonStudentCount, coinSlotAmount);
        player.addCoins(freeCoinSlotAmount - newFreeCoinSlotAmount);
      }
    }
  }

  /**
   * Set the chosen assistant card by the player as the current played card
   */
  static void pickAssistantCard(Player player, int cardIndex) {
    player.setPlayedCard(cardIndex);
  }

  /**
   * Removes the students from the given cloud and moves them to the given entrance
   */
  static void pickCloud(Cloud cloud, Dashboard dashboard) {
    dashboard.addToEntrance(cloud.getStudents());
    cloud.setStudents(new Students());
  }

  /**
   * Executes the movement: <br/>
   * - removes given students from src <br/>
   * - if the removing successes, adds given students to dest
   */
  static boolean placeStudents(StudentsMovement move) {
    if (move.src().removeStudentsFromSlot(move.students())) {
      move.dest().addStudentsToSlot(move.students());
      return true;
    }
    return false;
  }

  /**
   * Refills Clouds with the given students and removes them from the bag
   */
  static void refillClouds(StudentBag studentBag, List<Cloud> clouds, List<Students> cloudStudentsList) {
    for (int i = 0; i < clouds.size(); i++) {
      if (studentBag.removeStudents(cloudStudentsList.get(i)))
        clouds.get(i).setStudents(cloudStudentsList.get(i));
      modelLogger.debug(clouds.get(i).getStudents().toString());
    }
  }

  /**
   * If the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands. <br/>
   * If there isn't a new most influential player nothing changes <br/>
   * Modifies players' tower count if necessary. <br/>
   *
   * @param islandIndex Index of the island where to execute the operation
   * @param field       PLaying field of the game
   * @param players     All players playing the game
   */
  static void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players) {
    // Manages locked island
    if (field.getIsland(islandIndex).isLocked()) {
      modelLogger.info("Island {} is locked. No effect applied", islandIndex);
      field.getIsland(islandIndex).setLocked(false);
      modelLogger.info("Island {} unlocked", islandIndex);
      field.setLocks(field.getLocks() + 1);
      return;
    }

    Optional<TowerColor> mostInfluentialTeam = field.getMostInfluential(islandIndex);
    Island currIsland = field.getIsland(islandIndex);

    mostInfluentialTeam.ifPresent(bestTeam -> {
      // Set tower color
      Optional<TowerColor> oldColor = currIsland.getTowerColor();
      currIsland.setTowerColor(bestTeam);

      oldColor.ifPresentOrElse(oldTeam -> {
            // If a team is being dethroned
            if (!oldTeam.equals(bestTeam)) {
              modelLogger.info("Team {} is dethroning team {} on island {}", bestTeam, oldTeam, islandIndex);

              // Updates players dashboards
              players.forEach(p -> {
                // Remove towers from conquerors' dashboard
                if (p.getColorTeam().equals(bestTeam)) {
                  p.getDashboard().removeTowers(currIsland.getTowerCount());
                }

                // Add towers to conquered dashboard
                if (p.getColorTeam().equals(oldTeam)) {
                  p.getDashboard().addTowers(currIsland.getTowerCount());
                }
              });
            }
          },
          () -> {
            modelLogger.info("Island {} is being conquered for the first time by team {}", islandIndex, bestTeam);
            currIsland.setTowerCount(1);
            // Remove towers from conquerors' dashboard
            players.forEach(p -> {
              if (p.getColorTeam().equals(bestTeam)) {
                p.getDashboard().removeTowers(currIsland.getTowerCount());
              }
            });
          }
      );
      field.mergeIslands(islandIndex);
    });
  }
}
