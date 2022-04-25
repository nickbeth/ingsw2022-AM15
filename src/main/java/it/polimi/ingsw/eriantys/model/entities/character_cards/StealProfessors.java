package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StealProfessors implements CharacterCard {
  private static int cost = 2;
  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public int getCost() {
    return cost;
  }

  /**
   * Current player steals professors from owners if they have the same amount
   * of students.
   * @param gameState
   * @param gameService
   */
  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    cost = 3;
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);

    ProfessorHolder professorHolder = gameState.getPlayingField().getProfessorHolder();
    List<Dashboard> dashboards = new ArrayList<>();
    Dashboard currPlayerDash = gameState.getCurrentPlayer().getDashboard();
    gameState.getPlayers().forEach(p -> dashboards.add(p.getDashboard()));

    Arrays.stream(HouseColor.values()).forEach((color) -> {
      for (var dash : dashboards) {

        // If the curr player has the same amount of students in the dining hall of the other player
        if (currPlayerDash.getDiningHall().getCount(color) == dash.getDiningHall().getCount(color)
                // and that player had that specific professor
                && professorHolder.hasProfessor(dash.getTowers().color, color)) {
          // The curr player steals the professor
          professorHolder.setProfessorHolder(currPlayerDash.getTowers().color, color);
        }
      }
    });
  }
  /**
   * Checks:
   *  - if player has enough coins
   * @param gameState
   * @return
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
