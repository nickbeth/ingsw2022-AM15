package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentsMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;


public interface GameService {
  // TODO: test
  static void updateProfessors(ProfessorHolder professors, List<Dashboard> dashboards) {
    professors.updateProfessors(dashboards);
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
    if (field.getIsland(islandIndex).isLocked()) {
      field.getIsland(islandIndex).setLocked(false);
      field.setLocks(field.getLocks() + 1);
    } else {
      Optional<TowerColor> mostInfluentialTeam = field.getMostInfluential(islandIndex);
      Island currIsland = field.getIsland(islandIndex);

      if (mostInfluentialTeam.isPresent()) {
        // Set tower color
        TowerColor oldColor = currIsland.getTowerColor().get();
        currIsland.setTowerColor(mostInfluentialTeam.get());

        // If old color != new color => manage player towers
        if (!oldColor.equals(mostInfluentialTeam.get())) {
          for (Player p : players) {

            // Remove towers from conquerors' dashboard
            if (p.getColorTeam() == mostInfluentialTeam.get()) {
              p.getDashboard().removeTowers(currIsland.getTowerCount());
            }

            // Add towers to conquered dashboard
            if (p.getColorTeam() == oldColor) {
              p.getDashboard().addTowers(currIsland.getTowerCount());
            }
          }
        }
        field.mergeIslands(islandIndex);
      }
    }
  }
}
