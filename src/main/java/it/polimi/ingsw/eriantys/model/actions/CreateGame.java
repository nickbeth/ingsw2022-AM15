package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

public class CreateGame implements GameAction {
  private GameMode mode;
  private int playerCount;

  public CreateGame(GameMode mode, int playerCount) {
    this.mode = mode;
    this.playerCount = playerCount;
  }

  @Override
  public void apply(GameState gameState) {
    gameState = new GameState(playerCount, mode);
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
