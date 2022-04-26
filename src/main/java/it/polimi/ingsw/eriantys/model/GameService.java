package it.polimi.ingsw.eriantys.model;


import it.polimi.ingsw.eriantys.model.actions.StudentsMovement;
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

  @Override
  public void pickAssistantCard(Player player, int cardIndex) {
    player.setPlayedCard(cardIndex);
  }

  @Override
  public void pickCloud(Cloud cloud, Dashboard dashboard) {
    dashboard.addToEntrance(cloud.getStudents());
    cloud.setStudents(new Students());
  }

//  @Override
//  public void placeStudents(List<StudentsMovement> movements) {
//    movements.forEach((move) -> {
//      move.src().removeStudentFromSlot(move.studentColor());
//      move.dest().addStudentToSlot(move.studentColor());
//    });
//  }

  @Override
  public void placeStudent(StudentsMovement move) {
    move.src().removeStudentsFromSlot(move.students());
    move.dest().addStudentsToSlot(move.students());
  }


  @Override
  public void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players) {
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
