package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;

import java.util.List;

public class MoveMotherNature implements GameAction {
  private int amount;

  public MoveMotherNature(int amount) {
    this.amount = amount;
  }

  /**
   * Moves motherNature. <br/>
   * Applys motherNatureEffects on the island where mothernature resides<br>
   * Advances turnPhase.
   */
  @Override
  public void apply(GameState gameState) {
    PlayingField playingField = gameState.getPlayingField();
    playingField.moveMotherNature(amount);
    int motherNaturePos = playingField.getMotherNaturePosition();
    List<Player> players = gameState.getPlayers();
    GameService.applyMotherNatureEffect(motherNaturePos, playingField, players);
    gameState.advanceTurnPhase();
  }

  /**
   * checks:<br/>
   * - If the amount of movements is allowed<br/>
   *
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return amount > 0 &&
            gameState.getCurrentPlayer().getMaxMovement() >= amount;
  }
}
