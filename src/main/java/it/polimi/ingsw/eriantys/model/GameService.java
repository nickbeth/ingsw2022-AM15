package it.polimi.ingsw.eriantys.model;


import it.polimi.ingsw.eriantys.model.actions.StudentMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.List;
import java.util.Optional;

public class GameService implements IGameService {
  private static IGameService gameService;

  @Override
  public void refillClouds(StudentBag studentBag, List<Cloud> clouds, List<Students> cloudStudentsList) {
    for (int i = 0; i < clouds.size(); i++) {
      clouds.get(i).setStudents(cloudStudentsList.get(i));
      studentBag.removeStudents(cloudStudentsList.get(i));
    }
  }

  public static IGameService getGameService() {
    if (gameService == null) {
      gameService = new GameService();
    }
    return gameService;
  }

  /**
   * Removes 3 students of a certain HouseColor from the entrance of each player
   * then advances to next TurnPhase;
   */
  @Override
  public void dropStudents(List<Students> diningList, HouseColor color, int amount, StudentBag bag) {
    for (var entrance : diningList) {
      for (int i = 0; i < amount; i++) {
        if (entrance.getCount(color) != 0) {
          entrance.tryRemoveStudent(color);
          bag.addStudent(color);
        }
      }
    }
  }

  @Override
  public void ignoreColorInfluence(HouseColor ignoredColor, PlayingField playingField) {
    playingField.setIgnoredColor(ignoredColor);
  }

  @Override
  public void lockIsland(Island island) {
    island.setLocked(true);
  }

  @Override
  public void pickAssistantCard(Player player, int cardIndex) {
    player.setPlayedCard(cardIndex);
  }

  @Override
  public void pickCloud(Cloud cloud, Dashboard dashboard) {
    dashboard.addToEntrance(cloud.getStudents());
    cloud.setStudents(new Students());
  }

  @Override
  public void placeStudents(List<StudentMovement> movements) {
    movements.forEach((move) -> {
      move.src().removeStudentFromSlot(move.studentColor());
      move.dest().addStudentToSlot(move.studentColor());
    });
  }

  /**
   * If the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands. <br/>
   * If there isn't a new most influential player nothing changes <br/>
   * Modifies players' tower count if necessary. <br/>
   * @param islandIndex
   * @param field
   * @param players
   */
  @Override
  public void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players) {
    if (field.getIsland(islandIndex).isLocked()) {
      field.getIsland(islandIndex).setLocked(false);
      //TODO lock returns to the characterCard
    } else {
      Optional<TowerColor> mostInfluentialTeam = field.getMostInfluential(islandIndex);
      Island currIsland = field.getIsland(islandIndex);

      if (mostInfluentialTeam.isPresent()) {
        // Set tower color
        TowerColor oldColor = currIsland.getTowerColor();
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
