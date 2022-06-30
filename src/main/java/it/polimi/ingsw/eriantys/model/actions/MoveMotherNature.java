package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;

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
  public void apply(GameState game) {
    PlayingField playingField = game.getPlayingField();
    List<Player> players = game.getPlayers();

    // Moves mother nature
    playingField.moveMotherNature(amount);
    Integer motherNaturePos = playingField.getMotherNaturePosition();

    description = String.format("'%s' has moved mother nature %s islands.",
        game.getCurrentPlayer(), amount);

    // Apply her effect
    GameService.applyMotherNatureEffect(motherNaturePos, playingField, players);

    // Unset any possible previous character card played
    playingField.setPlayedCharacterCard(null);

    game.advance();

    if (game.isLastRound() && !game.isLastPlayer(game.getCurrentPlayer()))
      game.advance();
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
