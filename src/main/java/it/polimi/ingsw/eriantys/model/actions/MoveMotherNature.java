package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.List;

public class MoveMotherNature extends GameAction {
  private final int amount;

  public MoveMotherNature(int amount) {
    this.amount = amount;
  }

  /**
   * Moves motherNature. <br/>
   * Applies motherNatureEffects on the island where mother nature resides<br>
   * Advances turnPhase.
   */
  @Override
  public void apply(GameState gameState) {
    PlayingField playingField = gameState.getPlayingField();
    List<Player> players = gameState.getPlayers();

    // Moves mother nature
    playingField.moveMotherNature(amount);
    Integer motherNaturePos = playingField.getMotherNaturePosition();

    // Apply her effect
    GameService.applyMotherNatureEffect(motherNaturePos, playingField, players);

    gameState.advanceTurnPhase();
  }

  /**
   * checks:<br/>
   * - If the amount of movements is allowed<br/>
   * - If the GamePhase is ACTION & the TurnPhase is MOVING.
   *
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return amount > 0 && amount <= gameState.getCurrentPlayer().getMaxMovement();
  }
}
