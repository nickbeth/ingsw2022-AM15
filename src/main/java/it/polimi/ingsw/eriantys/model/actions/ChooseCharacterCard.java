package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class ChooseCharacterCard extends GameAction {
  private int ccIndex;

  public ChooseCharacterCard(int ccIndex) {
    this.ccIndex = ccIndex;
  }

  //
  @Override
  public void apply(GameState gameState) {
    PlayingField p = gameState.getPlayingField();
    p.setPlayedCharacterCard(ccIndex);
    if (!p.getPlayedCharacterCard().requiresInput()) {
      p.getPlayedCharacterCard().applyEffect(gameState);
      gameState.advanceTurnPhase();
    }
  }

  /**
   * @param gameState
   * @return false:
   * - If index is outof bounds
   * - If TurnPhase and ActionPhase aren't PLACING & ACTION
   */
  @Override
  public boolean isValid(GameState gameState) {
    return ccIndex >= 0 &&
        ccIndex < 3 &&
        gameState.getTurnPhase() == TurnPhase.PLACING &&
        gameState.getGamePhase() == GamePhase.ACTION;
  }
}
