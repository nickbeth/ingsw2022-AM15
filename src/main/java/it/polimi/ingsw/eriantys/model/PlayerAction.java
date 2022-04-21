package it.polimi.ingsw.eriantys.model;

public abstract class PlayerAction {
  protected String playerNickname;

  public abstract void apply(GameState gameState, IGameService gameService);
  public abstract boolean isValid(GameState gameState);
}
