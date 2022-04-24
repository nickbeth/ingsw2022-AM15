package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.GameAction;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

public class CreateGameState implements GameAction {
  private GameMode mode;
  private int playerCount;

  public CreateGameState(GameMode mode, int playerCount) {
    this.mode = mode;
    this.playerCount = playerCount;
  }

  @Override
  public void apply(GameState gameState, IGameService gameService) {
    gameState = new GameState(playerCount, mode);
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
