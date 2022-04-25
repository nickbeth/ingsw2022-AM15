package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class ChooseCharacterCard implements GameAction {
  private int ccIndex;

  public ChooseCharacterCard(int ccIndex) {
    this.ccIndex = ccIndex;
  }

  @Override
  public void apply(GameState gameState, IGameService gameService) {
    PlayingField p = gameState.getPlayingField();
    p.setCharacterCard(ccIndex);
    if(!p.getPlayedCharacterCard().requiresInput()) gameState.advanceTurnPhase();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return false;
  }
}
