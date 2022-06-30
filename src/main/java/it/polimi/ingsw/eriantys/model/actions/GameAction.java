package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;

/**
 * An action performed by a player that modifies the state of the game.
 * These objects are sent over the network and are applied to all clients' game states.
 */
public abstract class GameAction implements Serializable {
  protected String nickname = "";
  protected String description = "";

  public abstract void apply(GameState gameState);

  public abstract boolean isValid(GameState gameState);

  public String getNickname() {
    return nickname;
  }

  public String getDescription() {
    return description;
  }
}
