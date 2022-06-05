package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class PickCloud extends GameAction {

  private int cloudIndex;

  public PickCloud(int index) {
    this.cloudIndex = index;
  }

  //todo gestire meglio la disconnessione e testarla

  /**
   * Gets students from pickedCloud and puts them onto the players entrance,then advances turnPhase
   * ,if the current player is the last player it advances GamePhase , and then advances player.
   *
   * @param gameState
   */
  @Override
  public void apply(GameState gameState) {
    Cloud cloud = gameState.getPlayingField().getCloud(cloudIndex);
    Dashboard dashboard = gameState.getCurrentPlayer().getDashboard();

    // Checks if the player needs to pick a cloud. This is needed in case of in turn disconnection
    if (cloud.getStudents().getCount() + dashboard.getEntrance().getCount() == gameState.getRuleBook().entranceSize) {
      GameService.pickCloud(cloud, dashboard);
    }

    // Make the game advance its phases
    gameState.advanceTurnPhase();
    Player lastPlayer = gameState.getTurnOrderPlayers().get(gameState.getTurnOrderPlayers().size() - 1);
    if (gameState.getCurrentPlayer().equals(lastPlayer)) {
      // todo gestione disconnessione random del client prima di pickare la propria cloud
      gameState.advanceGamePhase();
    }
    gameState.getCurrentPlayer().unsetChosenCard();
    gameState.advancePlayer();
  }

  /**
   * Checks:<br>
   * If the cloud index is allowed<br>
   * If the picked cloud is empty<br>
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
