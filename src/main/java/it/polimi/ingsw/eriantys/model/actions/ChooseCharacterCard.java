package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;

public class ChooseCharacterCard implements GameAction {
  private int ccIndex;

  public ChooseCharacterCard(int ccIndex) {
    this.ccIndex = ccIndex;
  }
  //todo gestione del prezzo della CC
  @Override
  public void apply(GameState gameState) {
    PlayingField p = gameState.getPlayingField();
    p.setPlayedCharacterCard(ccIndex);
    if (!p.getPlayedCharacterCard().requiresInput()) {
      p.getPlayedCharacterCard().applyEffect(gameState);
      gameState.advanceTurnPhase();

    }
  }

  @Override
  public boolean isValid(GameState gameState) {
    return ccIndex >= 0 &&
            ccIndex < 3;
  }
}
