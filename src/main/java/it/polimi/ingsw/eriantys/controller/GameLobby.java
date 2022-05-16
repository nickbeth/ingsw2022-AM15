package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.HashMap;
import java.util.Map;

public class GameLobby {
  private GameMode mode;
  private int playerAmount;
  private Map<String, TowerColor> nicknameToTeamColor = new HashMap<>();

  public RuleBook getRules() {
    return RuleBook.makeRules(mode, nicknameToTeamColor.size());
  }

  public void addClient(String nickname) {
    nicknameToTeamColor.put(nickname, null);
  }

  public void setColor(String nickname, TowerColor teamColor) {
    nicknameToTeamColor.put(nickname, teamColor);
  }

  public void setMode(GameMode mode) {
    this.mode = mode;
  }

  public GameMode getMode() {
    return mode;
  }

  public void setPlayerAmount(int playerAmount) {
    this.playerAmount = playerAmount;
  }

  public int getPlayerAmount() {
    return playerAmount;
  }

  public Map<String, TowerColor> getNicknameToTeamColor() {
    return nicknameToTeamColor;
  }


}
