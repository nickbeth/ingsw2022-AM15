package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameInfo implements Serializable {
  /**
   * Enumeration of possible lobby states.
   */
  public enum LobbyState {
    WAITING, // Waiting for players to join
    STARTED, // Game has started
  }

  private GameMode mode;
  private int maxPlayerCount;
  private LobbyState lobbyState;

  /**
   * Map of player names to their chosen tower color.
   */
  private final Map<String, TowerColor> joinedPlayers = new HashMap<>();

  /**
   * Creates a set of rules from the previously set parameters.
   */
  public RuleBook makeRules() {
    return RuleBook.makeRules(mode, maxPlayerCount);
  }

  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }

  public void setMaxPlayerCount(int maxPlayerCount) {
    this.maxPlayerCount = maxPlayerCount;
  }

  public LobbyState getLobbyState() {
    return lobbyState;
  }

  public void setLobbyState(LobbyState lobbyState) {
    this.lobbyState = lobbyState;
  }

  public GameMode getMode() {
    return mode;
  }

  public void setMode(GameMode mode) {
    this.mode = mode;
  }

  public Map<String, TowerColor> getJoinedPlayers() {
    return joinedPlayers;
  }

  public Set<String> getPlayers() {
    return joinedPlayers.keySet();
  }

  public void addPlayer(String nickname, TowerColor towerColor) {
    joinedPlayers.put(nickname, towerColor);
  }

  public void addPlayer(String nickname) {
    joinedPlayers.put(nickname, null);
  }
}
