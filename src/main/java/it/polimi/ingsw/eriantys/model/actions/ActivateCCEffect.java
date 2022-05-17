package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class ActivateCCEffect implements GameAction {
  private CharacterCard cc;

  public ActivateCCEffect(CharacterCard cc) {
    this.cc = cc;
  }

  /**
   * Activate the effect of the played character card
   * todo Gestire il caso di carta già utilizzata sopra. Il chiamante deve già fornire una CC usata
   */
  @Override
  public void apply(GameState gameState) {
    gameState.getPlayingField().setPlayedCharacterCard(cc);
    gameState.getPlayingField().getPlayedCharacterCard().applyEffect(gameState);
    gameState.advanceTurnPhase();
  }

  /**
   * Checks:
   * - If the CC passed is buyable and playable
   * - If the gamePhase and TurnPhase are ACTION & EFFECT
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getPlayingField().getPlayedCharacterCard().isValid(gameState) &&
            gameState.getTurnPhase() == TurnPhase.EFFECT &&
            gameState.getGamePhase() == GamePhase.ACTION;
  }
}
