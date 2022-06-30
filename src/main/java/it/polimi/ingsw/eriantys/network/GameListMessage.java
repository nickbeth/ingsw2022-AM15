package it.polimi.ingsw.eriantys.network;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;

import java.util.Map;

/**
 * A message containing the list of games available to join.
 * This message is sent by the server until clients join a lobby.
 */
public class GameListMessage extends Message {
  public static class Builder {
    private Map<GameCode, GameInfo> gameList;

    public Builder() {
    }

    /**
     * Sets the game list of this message.
     *
     * @return The builder instance
     */
    public Builder gameList(Map<GameCode, GameInfo> gameList) {
      this.gameList = gameList;
      return this;
    }

    /**
     * Builds a new Message instance.
     *
     * @return A new Message instance
     */
    public GameListMessage build() {
      return new GameListMessage(gameList);
    }
  }

  private final Map<GameCode, GameInfo> gameList;

  private GameListMessage(Map<GameCode, GameInfo> gameList) {
    super(MessageType.GAMELIST);
    this.gameList = gameList;
  }

  public Map<GameCode, GameInfo> gameList() {
    return gameList;
  }
}
