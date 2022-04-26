package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.ArrayList;
import java.util.List;

public class DropStudents implements CharacterCard {
  private final HouseColor studentColor;
  private final static int DROP_STUDENTS_AMOUNT = 3;
  private final static int BASE_COST = 3;
  private final static int INCREMENTED_COST = 4;
  private static int cost = BASE_COST;

  public DropStudents(HouseColor color) {
    this.studentColor = color;
  }

  /**
   * Removes 3 students of a certain HouseColor from the entrance of each player
   * then advances to next TurnPhase;
   */
  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);

    List<Students> diningHallList = new ArrayList<>();
    gameState.getPlayers().forEach((player) ->
            diningHallList.add(player.getDashboard().getDiningHall())
    );
    StudentBag bag = gameState.getPlayingField().getStudentBag();
    gameService.dropStudents(diningHallList, studentColor, DROP_STUDENTS_AMOUNT, bag);
    cost = INCREMENTED_COST;
  }

  @Override
  public boolean requiresInput() {
    return true;
  }
  public int getCost() {
    return cost;
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
