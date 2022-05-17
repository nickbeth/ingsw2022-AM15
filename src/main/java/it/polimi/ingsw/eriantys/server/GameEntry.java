package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.controller.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.network.Client;

import java.util.Collection;
import java.util.HashMap;

public class GameEntry {
  private final GameInfo gameInfo;
  private final HashMap<String, Client> players;
  private final GameState gameState;
  private final ActionInvoker actionInvoker;

  public GameEntry(GameInfo gameInfo) {
    this.players = new HashMap<>();
    this.gameInfo = gameInfo;
    this.gameState = new GameState(gameInfo.getMaxPlayerCount(), gameInfo.getMode());
    this.actionInvoker = new ActionInvoker(gameState);
  }

  public boolean executeAction(GameAction action) {
    return actionInvoker.executeAction(action);
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

  public void setPlayerColor(String nickname, TowerColor towerColor) {
    gameInfo.addPlayer(nickname, towerColor);
  }

  public void removePlayer(String nickname) {
    players.remove(nickname);
  }

  public String getCurrentPlayer() {
    return gameState.getCurrentPlayer().getNickname();
  }

  public boolean isFull() {
    return players.size() == gameInfo.getMaxPlayerCount();
  }

  public GameInfo getGameInfo() {
    return gameInfo;
  }

  public GameState getGameState() {
    return gameState;
  }
}
