package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class PickCloud implements GameAction {
  private int cloudIndex;
  private String playerNickname;

  public PickCloud(String nickname, int index) {
    this.cloudIndex = index;
    this.playerNickname = nickname;
  }

  /**
   * Gets students from pickedCloud and puts them onto the players entrance,then advances turnPhase
   * ,if its the last player it advances GamePhase , and then advances player.
   *
   * @param gameState
   * @param gameService
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    Cloud cloud = gameState.getPlayingField().getCloud(cloudIndex);
    Dashboard dashboard = gameState.getCurrentPlayer().getDashboard();

    gameService.pickCloud(cloud, dashboard);

    // Make the game advance its phases
    gameState.advanceTurnPhase();
    Player lastPlayer = gameState.getTurnOrderPlayers().get(gameState.getTurnOrderPlayers().size() - 1);
    if (gameState.getCurrentPlayer().equals(lastPlayer)) {
      gameState.advanceGamePhase();
    }
    gameState.advancePlayer();
  }

  /**
   * Checks:<br>
   * If currentPlayer is the player who did the action<br>
   * If the gamePhase is ACTION<br>
   * If the turnPhase is PICKING<br>
   * If the cloud index is allowed
   * If the picked cloud is empty<br>
   *
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.PICKING &&
            cloudIndex >= 0 &&
            cloudIndex < gameState.getRuleBook().cloudCount &&
            !gameState.getPlayingField().getCloud(cloudIndex).isEmpty();
  }
}