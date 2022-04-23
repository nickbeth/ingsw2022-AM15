package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class LockIsland implements GameAction {
  private int index;
  private String playerNickname;

  public LockIsland(String nickname, int index) {
    this.playerNickname = nickname;
    this.index = index;
  }

  /**
   * Locks an island and advances TurnPhase
   * @param gameState
   * @param gameService
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    gameService.lockIsland(gameState.getPlayingField().getIsland(index));
    gameState.advanceTurnPhase();
  }
  /**
   * Checks:<br>
   * - if current player is the player who did the action<br>
   * - if the gamePhase is ACTION<br>
   * - if the turnPhase is EFFECT<br>
   * - if the chosen index is allowed<br>
   * - if the chosen island is already locked<br>
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.EFFECT &&
            gameState.getPlayingField().getIslandsAmount() > index &&
            index >= 0 &&
            gameState.getPlayingField().getIsland(index).isLocked();
  }
}
