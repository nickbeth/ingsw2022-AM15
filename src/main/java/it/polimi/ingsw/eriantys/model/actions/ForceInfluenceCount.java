package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;


public class ForceInfluenceCount implements GameAction {
  private int islandIndex;
  private String playerNickname;

  public ForceInfluenceCount(String nickname, int islandIndex) {
    this.islandIndex = islandIndex;
    this.playerNickname = nickname;
  }

  /**
   * If the selected island is not locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands.
   * If there isn't a new most influential player nothing changes. <br/>
   * Modifies players' tower count if necessary. <br/>
   * It advances turnPhase.
   * @param gameState
   * @param gameService
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    gameService.applyMotherNatureEffect(islandIndex, gameState.getPlayingField(), gameState.getPlayers());
    gameState.advanceTurnPhase();
  }
  /**
   * checks:
   * If currentPlayer is the player who did the action<br>
   * If the gamePhase is ACTION<br>
   * If the turnPhase is EFFECT<br>
   * If the Island index is allowed
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.EFFECT &&
            gameState.getPlayingField().getIslandsAmount() > islandIndex &&
            islandIndex >= 0;
  }
}
