package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Structure that contains general pieces of information about a specific game
 * such as if it's started or not, the players' nickname involved or the mode.
 */
public class GameInfo implements Serializable {
  /**
   * Enumeration of possible lobby states.
   */
  public enum LobbyState {
    WAITING, // Waiting for players to join
    STARTED, // Game has started
  }

  private final GameMode mode;
  private final int maxPlayerCount;
  private LobbyState lobbyState = LobbyState.WAITING;
  /**
   * Map of player names to their chosen tower color.
   */
  private final Map<String, TowerColor> joinedPlayers = new HashMap<>();

  public GameInfo(GameInfo info) {
    this.lobbyState = info.lobbyState;
    this.mode = info.mode;
    this.maxPlayerCount = info.maxPlayerCount;
    joinedPlayers.putAll(info.joinedPlayers);
  }

  public GameInfo(int numberOfPlayers, GameMode mode) {
    this.maxPlayerCount = numberOfPlayers;
    this.mode = mode;
  }

  /**
   * Checks whether the game is ready to start, by checking that the number of player has been reached and that selected tower colors are valid.
   *
   * @return {@code true} if the game is ready to start, {@code false} otherwise
   */
  public boolean isReady() {
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
   */
  public void start() {
    lobbyState = LobbyState.STARTED;
  }

  public boolean isStarted() {
    return lobbyState == LobbyState.STARTED;
  }

  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }

  public GameMode getMode() {
    return mode;
  }

  public Map<String, TowerColor> getPlayersMap() {
    return joinedPlayers;
  }

  public Set<String> getJoinedPlayers() {
    return joinedPlayers.keySet();
  }

  public void addPlayer(String nickname, TowerColor towerColor) {
    joinedPlayers.put(nickname, towerColor);
  }

  public void addPlayer(String nickname) {
    joinedPlayers.put(nickname, null);
  }

  public void removePlayer(String nickname) {
    joinedPlayers.remove(nickname);
  }

  public TowerColor getPlayerColor(String nickname) {
    return joinedPlayers.get(nickname);
  }

  public boolean isFull() {
    return joinedPlayers.size() >= maxPlayerCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;

    GameInfo gameInfo = (GameInfo) o;
    return maxPlayerCount == gameInfo.maxPlayerCount &&
        mode == gameInfo.mode &&
        lobbyState == gameInfo.lobbyState &&
        joinedPlayers.equals(gameInfo.joinedPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, maxPlayerCount, lobbyState, joinedPlayers);
  }
}
