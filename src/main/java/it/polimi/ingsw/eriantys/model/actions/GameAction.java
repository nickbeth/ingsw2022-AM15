package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.Serializable;


// TODO: Fix RefillCloud

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
