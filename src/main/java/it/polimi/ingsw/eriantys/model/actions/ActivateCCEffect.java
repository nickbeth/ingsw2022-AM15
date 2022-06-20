package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

public class ActivateCCEffect extends GameAction {
  private final CharacterCard cc;

  public ActivateCCEffect(CharacterCard cc) {
    this.cc = cc;
  }

  /**
   * Activate the effect of the played character card
   * todo Gestire il caso di carta già utilizzata sopra. Il chiamante deve già fornire una CC usata
   */
  @Override
  public void apply(GameState gameState) {
    PlayingField p = gameState.getPlayingField();
    p.setPlayedCharacterCard(cc);
    p.getPlayedCharacterCard().applyEffect(gameState);
  }

  /**
   * Checks:
   * - If the CC passed is purchasable and playable
   * - If the gamePhase and TurnPhase are ACTION & EFFECT
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getPlayingField().getPlayedCharacterCard().isValid(gameState) &&
        gameState.getGamePhase() == GamePhase.ACTION;
  }
}
