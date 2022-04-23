package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.Students;

import java.util.List;

import static it.polimi.ingsw.eriantys.model.RuleBook.*;

public class InitiateGameEntities implements GameAction {
  private List<Students> entrances;
  private List<Students> islands;
  private List<Students> clouds;

  public InitiateGameEntities(List<Students> entrances, List<Students> islands, List<Students> clouds) {
    this.entrances = entrances;
    this.islands = islands;
    this.clouds = clouds;
  }

  /**
   * Initiate students in the entrances, islands and clouds.
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    // Initiate players entrances
    List<Player> players = gameState.getPlayers();
    for (int i = 0; i < entrances.size(); i++) {
      players.get(i).getDashboard().getEntrance().setStudents(entrances.get(i));
      gameState.getPlayingField().getStudentBag().removeStudents(entrances.get(i));
    }
    // Initiate islands' students
    for (int i = 0; i < islands.size(); i++) {
      gameState.getPlayingField().getIsland(i).getStudents().setStudents(islands.get(i));
      gameState.getPlayingField().getStudentBag().removeStudents(islands.get(i));
    }
    // Initiate clouds' students
    gameService.refillClouds(
            gameState.getPlayingField().getStudentBag(),
            gameState.getPlayingField().getClouds(),
            clouds);
  }

  @Override
  public boolean isValid(GameState gameState) {
    RuleBook ruleBook = gameState.getRuleBook();
    return entrances.size() == ruleBook.entranceSize &&
            entrances.stream().allMatch((students) -> students.getCount() == ruleBook.entranceSize) &&
            islands.size() == ISLAND_COUNT &&
            islands.stream().allMatch((students) -> students.getCount() == INITIAL_ISLAND_STUDENTS) &&
            clouds.size() == ruleBook.entranceSize &&
            clouds.stream().allMatch((students) -> students.getCount() == ruleBook.playableStudentCount)
            ;

  }
}
