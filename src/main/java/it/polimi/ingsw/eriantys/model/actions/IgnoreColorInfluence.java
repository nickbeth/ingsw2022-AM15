package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class IgnoreColorInfluence extends PlayerAction {
  HouseColor ignoredColor;

  public IgnoreColorInfluence(String nickname, HouseColor ignoredColor) {
    this.playerNickname = nickname;
    this.ignoredColor = ignoredColor;
  }

  /**
   * Ignores selected HouseColor in the next influence count
   * @param gameState
   */
  @Override
  public void apply(GameState gameState) {
    gameState.getPlayingField().setIgnoredColor(ignoredColor);
    gameState.advanceTurnPhase();
  }

  /**
   * checks:
   * If currentPlayer is the player who did the action<br>
   * If the gamePhase is ACTION<br>
   * If the turnPhase is EFFECT<br>
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.EFFECT;
  }
}
