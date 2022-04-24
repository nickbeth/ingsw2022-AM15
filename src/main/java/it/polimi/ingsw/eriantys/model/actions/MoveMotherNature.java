package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.List;

public class MoveMotherNature implements GameAction {
  private int amount;
  private String playerNickname;

  public MoveMotherNature(String nickname, int amount) {
    this.playerNickname = nickname;
    this.amount = amount;
  }

  /**
   * Moves motherNature. <br/>
   * If the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands. <br/>
   * If there isn't a new most influential player nothing changes <br/>
   * Modifies players' tower count if necessary. <br/>
   * It advances turnPhase.
   *
   * @param gameState
   * @param gameService
   */
  @Override
  public void apply(GameState gameState, IGameService gameService) {
    PlayingField playingField = gameState.getPlayingField();
    playingField.moveMotherNature(amount);
    int motherNaturePos = playingField.getMotherNaturePosition();
    List<Player> players = gameState.getPlayers();
    gameState.getCurrentPlayer().unsetChosenCard();
    gameService.applyMotherNatureEffect(motherNaturePos, playingField, players);
  }

  /**
   * checks:<br/>
   * - If current player is the player who did the action<br/>
   * - If the gamePhase is ACTION<br/>
   * - If the turnPhase is MOVING<br/>
   * - If the amount of movements is allowed<br/>
   *
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.MOVING &&
            amount > 0 &&
            gameState.getCurrentPlayer().getMaxMovement() >= amount;
  }
}
