package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

public class GameEntry {
  private final GameInfo gameInfo; //!< GameInfo of this game
  private final HashMap<String, Client> clients; //!< A map of clients connected to this game and their respective names
  private final GameState gameState; //!< GameState of this game

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
}
