package it.polimi.ingsw.eriantys.model;

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
  private LobbyState lobbyState = LobbyState.WAITING;

  /**
   * Map of player names to their chosen tower color.
   */
  private final Map<String, TowerColor> joinedPlayers = new HashMap<>();

  /**
   * Checks if the game is ready to start.
   *
   * @return {@code true} if the game is ready to start, {@code false} otherwise
   */
  private boolean isReady() {
    boolean cond = !joinedPlayers.containsValue(null) && joinedPlayers.size() == maxPlayerCount;
    if (maxPlayerCount == 4) {
      for (TowerColor color : joinedPlayers.values())
        cond &= joinedPlayers.values().stream().filter(x -> x == color).count() == 2;
    }
    return cond;
  }

  /**
   * Checks if the given tower color can be chosen by the given player.
   *
   * @param nickname   The player's nickname
   * @param towerColor The chosen tower color
   * @return {@code true} if the tower color can be chosen by the player, {@code false} otherwise
   */
  public boolean isTowerColorValid(String nickname, TowerColor towerColor) {
    if (maxPlayerCount == 4) {
      return joinedPlayers.values().stream().filter((it) -> it == towerColor).count() < 2;
    } else {
      return !joinedPlayers.containsValue(towerColor);
    }
  }

  /**
   * Sets this game as started.
   *
   * @return {@code true} if the game was successfully started, {@code false} otherwise
   */
  public boolean start() {
    if (isReady()) {
      lobbyState = LobbyState.STARTED;
      return true;
    }
    return false;
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

  public TowerColor getPlayerColor(String nickname) {
    return joinedPlayers.get(nickname);
  }
}
