package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

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
    modelLogger.info("Character card {} chosen", ccIndex);
  }

  /**
   * @return False: <br>
   * - If index is out of bounds <br>
   * - If TurnPhase and ActionPhase aren't PLACING & ACTION<br>
   */
  @Override
  public boolean isValid(GameState gameState) {
    return ccIndex >= 0 &&
        ccIndex < 3 &&
        gameState.getTurnPhase() == TurnPhase.PLACING &&
        gameState.getGamePhase() == GamePhase.ACTION;
  }
}
