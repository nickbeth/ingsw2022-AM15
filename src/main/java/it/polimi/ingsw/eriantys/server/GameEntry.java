package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

public class GameEntry {
  private final GameInfo gameInfo;
  private final HashMap<String, Client> players;
  private final GameState gameState;

  public GameEntry(GameInfo gameInfo) {
    this.players = new HashMap<>();
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
        gameState.checkWinCondition();
        return true;
      }
      return false;
    }
  }

  /**
   * Sends a message to all clients in this lobby.
   *
   * @param message The message to broadcast
   */
  public void broadcastMessage(Message message) {
    serverLogger.debug("Broadcasting message to {} clients: '{}'", getClients().size(), message);
    getClients().forEach(client -> client.send(message));
  }

  public Client getClient(String nickname) {
    return players.get(nickname);
  }

  public Collection<Client> getClients() {
    return players.values();
  }

  public boolean hasPlayer(String nickname) {
    return players.containsKey(nickname);
  }

  public void addPlayer(String nickname, Client client) {
    players.put(nickname, client);
    gameInfo.addPlayer(nickname);
  }

  public void removePlayer(String nickname) {
    players.remove(nickname);
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
    players.remove(nickname);
    gameState.getPlayer(nickname).setConnected(false);
  }

  public void reconnectPlayer(String nickname, Client client) {
    players.put(nickname, client);
    gameState.getPlayer(nickname).setConnected(true);
  }

  public String getCurrentPlayer() {
    return gameState.getCurrentPlayer().getNickname();
  }

  public boolean isFull() {
    return players.size() == gameInfo.getMaxPlayerCount();
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
