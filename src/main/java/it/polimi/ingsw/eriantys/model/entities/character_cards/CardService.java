package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public interface CardService {
  /**
   * Adds the given amount points to the current Team influence
   *
   * @param amount   Amount to add
   * @param islands  Islands Set
   * @param currTeam Current team playing
   */
  static void addToInfluence(int amount, List<Island> islands, TowerColor currTeam) {
    List<TeamsInfluenceTracer> teamsInfluenceList = new ArrayList<>();

    islands.forEach((island) ->
        teamsInfluenceList.add(island.getTeamsInfluenceTracer()));

    teamsInfluenceList.forEach((teamsInfluence) -> {
      int modifiedValue = teamsInfluence.getInfluence(currTeam) + amount;
      modelLogger.debug("New value: " + modifiedValue);
      teamsInfluence.setInfluence(currTeam, modifiedValue);
    });
  }

  /**
   * Modified the influences based on the ignored color. Influence must be updated first
   *
   * @param islands                    Islands Set
   * @param ignoredColor
   * @param teamOwningIgnoredProfessor
   */
  static void ignoreColor(List<Island> islands, HouseColor ignoredColor, TowerColor teamOwningIgnoredProfessor) {
// For each island updates teams' influence based on the ignored color
    if (teamOwningIgnoredProfessor == null)
      return;
    islands.forEach((island) -> {
      TeamsInfluenceTracer influenceTracer = island.getTeamsInfluenceTracer();
      Integer ignoredStudentsCount = island.getStudents().getCount(ignoredColor);
      influenceTracer.setInfluence(teamOwningIgnoredProfessor, influenceTracer.getInfluence(teamOwningIgnoredProfessor) - ignoredStudentsCount);
    });
  }

  /**
   * Modified the influences based on ignoring towers. Influence must be updated first
   *
   * @param islands Islands set
   */
  static void ignoreTowers(List<Island> islands) {
    // For each island, updates the influence by reducing it by the number of tower for the team conqueror
    islands.forEach(island -> {
      int modifier = 0;
      Optional<TowerColor> conquerorTeam = island.getTowerColor();
      TeamsInfluenceTracer teamsInfluence = island.getTeamsInfluenceTracer();
      if (conquerorTeam.isPresent()) {
        modifier -= island.getTowerCount();
        teamsInfluence.setInfluence(conquerorTeam.get(), teamsInfluence.getInfluence(conquerorTeam.get()) + modifier);
      }
    });
  }

  /**
   * Removes the amount of students from all the diningHall given. <br>
   * If not enough students their count would go to zero
   *
   * @param diningList List of Students present in the player diningHall
   * @param color      Student color to be dropped
   * @param amount     Amount students to drop
   * @param bag        Student bag where to insert the dropped students
   */
  static void dropStudents(List<Students> diningList, HouseColor color, int amount, StudentBag bag) {
    for (var entrance : diningList) {
      for (int i = 0; i < amount; i++) {
        if (entrance.getCount(color) != 0) {
          entrance.tryRemoveStudent(color);
          bag.addStudent(color);
        }
      }
    }
  }

  /**
   * Force Mother Nature effects
   *
   * @param islandIndex
   * @param field
   * @param players
   */
  static void forceMotherNatureEffects(int islandIndex, PlayingField field, List<Player> players) {
    if (field.getIsland(islandIndex).isLocked()) {
      field.getIsland(islandIndex).setLocked(false);
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

  /**
   * Give the chance to exceed the limit of Mother Nature's movement by the amount
   *
   * @param currPlayer
   * @param amount
   */
  static void addToMotherNatureMoves(Player currPlayer, int amount) {
    currPlayer.addToMaxMovement(amount);
  }

  /**
   * Lock the given island
   *
   * @param island
   */
  static void lockIsland(Island island) {
    island.setLocked(true);
  }

  /**
   * Change the holders of the professor even if the curr Dashboard has only<br/> the same amount of students in the dining hall of other player
   *
   * @param currDashboard
   * @param dashboards
   * @param professorHolder
   */
  static void stealProfessor(Dashboard currDashboard, List<Dashboard> dashboards, ProfessorHolder professorHolder) {
    Arrays.stream(HouseColor.values()).forEach((color) -> {
      for (var dash : dashboards) {

        // If the curr player has the same amount of students in the dining hall of the other player
        if (currDashboard.getDiningHall().getCount(color) == dash.getDiningHall().getCount(color)
            // and that player had that specific professor
            && professorHolder.hasProfessor(dash.getTowers().color, color)) {
          // The curr player steals the professor
          professorHolder.setProfessorHolder(currDashboard.getTowers().color, color);
        }
      }
    });
  }
}
