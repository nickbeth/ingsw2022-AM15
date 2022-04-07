package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

/**
 * Set played the assistant card chosen by the Player.
 */
public class PickAssistantCard extends PlayerAction {
  private final int cardIndex;

  public PickAssistantCard(int cardIndex, String nick) {
    this.cardIndex = cardIndex;
    this.playerNickname = nick;
  }

  // todo javadoc e testing
  @Override
  public void apply(GameState gameState) {
    gameState.getCurrentPlayer().setPlayedCard(cardIndex);
    gameState.advancePlayer();

    // If all players have set the card the game continues
    Player lastPlayer = gameState.getPlayers().get(gameState.getPlanOrder().size() - 1);
    if (gameState.getCurrentPlayer().equals(lastPlayer))
      gameState.advanceGamePhase();
  }


  // todo javadoc e testing
  @Override
  public boolean isValid(GameState gameState) {
    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    // check if it's PLANNING phase
    if (!gameState.getGamePhase().equals(GamePhase.PLANNING)) return false;

    // Checks if the card with the same value has been already played
    AssistantCard chosenCard = gameState.getCurrentPlayer().getCards().get(cardIndex);
    for (Player p : gameState.getPlayers()) {
      if (!p.getNickname().equals(playerNickname)) {
        // The only case it's acceptable to play the same assistant card it's when
        // the player has no other cards left.
        // If it's not the case return false.
        if (!(p.getTurnPriority() == chosenCard.value &&
                gameState.getCurrentPlayer().getCards().size() == 1)) {
          return false;
        }
      }
    }
    return true;
  }
}
