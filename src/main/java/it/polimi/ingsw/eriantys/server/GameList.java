package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.network.Client;
import org.javatuples.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread-safe container for the list of active games.
 */
public class GameList implements Map<GameCode, GameEntry> {
  private final ConcurrentMap<GameCode, GameEntry> activeGames;

  /**
   * A list of join-able games, sent to new clients when they connect to ease joining a game.
   */
  private final ConcurrentMap<GameCode, GameInfo> joinableGameList;

  public GameList() {
    this.activeGames = new ConcurrentHashMap<>();
    this.joinableGameList = new ConcurrentHashMap<>();
  }

  /**
   * Creates a new game entry in the games list.
   *
   * @param gameInfo The game info of the game to create
   * @return The game code of the newly created game and the newly created game entry
   */
  public Pair<GameCode, GameEntry> create(GameInfo gameInfo) {
    GameCode gameCode = GameCode.generateUnique(keySet());
    GameEntry gameEntry = new GameEntryProxy(gameInfo, gameCode);

    activeGames.put(gameCode, gameEntry);
    joinableGameList.put(gameCode, gameInfo);

    return Pair.with(gameCode, gameEntry);
  }

  public Map<GameCode, GameInfo> getJoinableGameList() {
    return Collections.unmodifiableMap(joinableGameList);
  }

  @Override
  public int size() {
    return activeGames.size();
  }

  @Override
  public boolean isEmpty() {
    return activeGames.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return activeGames.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return activeGames.containsValue(value);
  }

  /**
   * Returns the game entry associated with the given game code.
   *
   * @param key The game code of the game
   * @return The game entry associated with the given game code, or {@code null} if no game entry exists for the given game code
   */
  @Override
  public GameEntry get(Object key) {
    return activeGames.get(key);
  }

  @Override
  public GameEntry put(GameCode key, GameEntry value) {
    throw new UnsupportedOperationException("Direct insertion of game entries is not supported");
  }

  /**
   * Removes the game with the given code from the games list.
   *
   * @param key The code of the game to remove
   */
  @Override
  public GameEntry remove(Object key) {
    joinableGameList.remove(key);
    return activeGames.remove(key);
  }

  @Override
  public void putAll(Map<? extends GameCode, ? extends GameEntry> m) {
    throw new UnsupportedOperationException("Direct insertion of game entries is not supported");
  }

  @Override
  public void clear() {
    joinableGameList.clear();
    activeGames.clear();
  }

  @Override
  public Set<GameCode> keySet() {
    return activeGames.keySet();
  }

  @Override
  public Collection<GameEntry> values() {
    return activeGames.values();
  }

  @Override
  public Set<Entry<GameCode, GameEntry>> entrySet() {
    return activeGames.entrySet();
  }

  /**
   * A proxy for a game entry, used to ease keeping track of join-able games.
   */
  private class GameEntryProxy extends GameEntry {
    private final GameCode gameCode;

    public GameEntryProxy(GameInfo gameInfo, GameCode gameCode) {
      super(gameInfo);
      this.gameCode = gameCode;
    }

    @Override
    public void addPlayer(String nickname, Client client) {
      super.addPlayer(nickname, client);
      // Remove the game from the join-able list if it is full, as no more players can join it
      if (getGameInfo().isFull())
        joinableGameList.remove(gameCode);
    }

    @Override
    public void removePlayer(String nickname) {
      // Only add this game back to the join-able list if it was full before removing the player
      if (getGameInfo().isFull())
        joinableGameList.put(gameCode, getGameInfo());
      super.removePlayer(nickname);
    }

    @Override
    public void start() {
      super.start();
      joinableGameList.remove(gameCode);
    }
  }
}
