package it.polimi.ingsw.eriantys.network;

/**
 * Enumeration of all possible {@link Message} types.
 */
public enum MessageType {
  /**
   * Heartbeat message, sent by the server to clients
   * <p>
   * No populated fields.
   */
  PING,

  /**
   * Heartbeat message, sent by clients as a reply to {@link MessageType#PING}
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  PONG,

  /**
   * Sent by clients to choose a nickname.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  NICKNAME_REQUEST,

  /**
   * Sent by the server as a successful reply to {@link MessageType#NICKNAME_REQUEST}.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  NICKNAME_OK,

  /**
   * Sent by client to request a list of games available to join.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  GAMELIST_REQUEST,

  /**
   * Sent by the server as a successful reply to {@link MessageType#GAMELIST_REQUEST}, containing a list of available games to join.
   * <p>
   * Populated fields: <p>
   * {@link GameListMessage#gameList()}
   */
  GAMELIST,

  /**
   * Sent by clients to create a new game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameInfo()}
   */
  CREATE_GAME,

  /**
   * Sent by clients to join a game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameCode()}
   */
  JOIN_GAME,

  /**
   * Sent by clients to leave a game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameCode()}, {@link Message#gameInfo()}
   */
  SELECT_TOWER,

  /**
   * Sent by clients to leave a game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameCode()}
   */
  QUIT_GAME,

  /**
   * Sent by the server as a reply to
   * {@link MessageType#CREATE_GAME CREATE_GAME},
   * {@link MessageType#JOIN_GAME JOIN_GAME},
   * {@link MessageType#SELECT_TOWER SELECT_TOWER} and
   * {@link MessageType#JOIN_GAME JOIN_GAME}.
   * <p>
   * Populated fields: <p>
   * {@link Message#gameCode()}, {@link Message#gameInfo()}
   */
  GAMEINFO,

  /**
   * Sent by clients to start the game. The server then broadcasts this message back to all clients in the lobby.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameCode()}, {@link Message#gameInfo()}, {@link Message#gameAction()}
   */
  START_GAME,

  /**
   * Sent by the server when a client joins a game they had disconnected from.
   * <p>
   * Populated fields: same as {@link MessageType#START_GAME}
   *
   * @see MessageType#START_GAME
   */
  START_GAME_RECONNECTED,

  /**
   * Sent by clients to perform a game action.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#gameCode()}, {@link Message#gameAction()}
   */
  PLAY_ACTION,

  /**
   * Sent by the server as a successful reply to {@link MessageType#PLAY_ACTION PLAY_ACTION}.
   * Broadcasts the performed action to all clients in a lobby.
   * <p>
   * Populated fields: <p>
   * {@link Message#gameCode()}, {@link Message#gameAction()}
   */
  GAMEDATA,

  /**
   * Sent by the server when a game reaches a win condition and ends.
   * When this message is received by clients, the game should be considered as deleted on the server.
   * <p>
   * Populated fields: <p>
   * {@link Message#gameCode()}
   */
  END_GAME,

  /**
   * Sent by the server when a player has disconnected from the game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  PLAYER_DISCONNECTED,

  /**
   * Sent by the server when a player has reconnected to the game.
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}
   */
  PLAYER_RECONNECTED,

  /**
   * Sent by the server as an unsuccessful reply to any message.
   * <p>
   * Populated fields: <p>
   * {@link Message#error()}
   */
  ERROR,

  /**
   * Internal queue-handler communication for local network-related events, these must never be sent over the network
   * <p>
   * Populated fields: <p>
   * {@link Message#nickname()}, {@link Message#error()}
   */
  INTERNAL_SOCKET_ERROR,
}
