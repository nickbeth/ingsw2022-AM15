package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

/**
 * An entry in the active games' collection, containing the game state and the clients connected to it.
 */
public class GameEntry {
  /**
   * GameInfo of this game
   */
  private final GameInfo gameInfo;

  /**
   * A map from a player's name to the {@link Client client} they're using to communicate
   */
  private final HashMap<String, Client> clients;

  /**
   * The {@link GameState} of this game
   */
  private final GameState gameState;

  /**
   * The scheduled future of the deletion of this game, used to cancel the deletion if players joins back
   */
  private ScheduledFuture<?> deletionSchedule;

  public GameEntry(GameInfo gameInfo) {
    this.clients = new HashMap<>();
    this.gameInfo = gameInfo;
    this.gameState = new GameState(gameInfo.getMaxPlayerCount(), gameInfo.getMode());
  }

  /**
   * Applies the given {@link GameAction} to the game state.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if the action was valid and was applied successfully, {@code false} otherwise
   */
  public boolean executeAction(GameAction action) {
    synchronized (gameState) {
      if (action.isValid(gameState)) {
        action.apply(gameState);
        serverLogger.debug("Action {} applied to game ", action.getClass().getSimpleName());
        return true;
      }
      return false;
    }
  }

  public boolean checkWinCondition() {
    return gameState.checkWinCondition();
  }

  public Client getClient(String nickname) {
    return clients.get(nickname);
  }

  public Collection<Client> getClients() {
    return clients.values();
  }

  public Set<String> getClientNames() {
    return clients.keySet();
  }

  public boolean hasPlayer(String nickname) {
    return clients.containsKey(nickname);
  }

  public void addPlayer(String nickname, Client client) {
    clients.put(nickname, client);
    gameInfo.addPlayer(nickname);
  }

  public void removePlayer(String nickname) {
    clients.remove(nickname);
    gameInfo.removePlayer(nickname);
  }

  public void setPlayerColor(String nickname, TowerColor towerColor) {
    gameInfo.addPlayer(nickname, towerColor);
  }

  public void initPlayers() {
    for (Map.Entry<String, TowerColor> entry : gameInfo.getPlayersMap().entrySet()) {
      gameState.addPlayer(entry.getKey(), entry.getValue());
    }
  }

  public void disconnectPlayer(String nickname) {
    clients.remove(nickname);
    gameState.getPlayer(nickname).setConnected(false);
  }

  public void reconnectPlayer(String nickname, Client client) {
    clients.put(nickname, client);
    gameState.getPlayer(nickname).setConnected(true);
  }

  public void cancelDeletion() {
    if (deletionSchedule != null) {
      deletionSchedule.cancel(false);
      deletionSchedule = null;
    }
  }

  public String getCurrentPlayer() {
    return gameState.getCurrentPlayer().getNickname();
  }

  public boolean isFull() {
    return clients.size() == gameInfo.getMaxPlayerCount();
  }

  public boolean isStarted() {
    return gameInfo.isStarted();
  }

  public GameInfo getGameInfo() {
    return gameInfo;
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setDeletionSchedule(ScheduledFuture<?> deletionSchedule) {
    this.deletionSchedule = deletionSchedule;
  }
}
