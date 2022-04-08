package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class lockIsland extends PlayerAction{
  int index;
  public lockIsland(String nickname, int index) {
    this.playerNickname = nickname;
    this.index = index;
  }

  /**
   * Locks an island and advances TurnPhase
   * @param gameState
   */
  @Override
  public void apply(GameState gameState) {
    gameState.getPlayingField().getIsland(index).setLocked(true);
    gameState.advanceTurnPhase();
  }
  /**
   * checks:<br>
   * If current player is the player who did the action<br>
   * If the gamePhase is ACTION<br>
   * If the turnPhase is EFFECT<br>
   * If the chosen index is allowed
   * If the chosen island is already locked<br>
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
