package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;

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
   * Checks if the CC passed is buyable and playable
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getPlayingField().getPlayedCharacterCard().isValid(gameState);
  }
}
