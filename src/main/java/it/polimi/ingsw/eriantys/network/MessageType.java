package it.polimi.ingsw.eriantys.network;

/**
 * This class enumerates all the possible message types.
 *
 * The commands are grouped by game phase (lobby, playing),
 * and are further divided into client commands followed by the type of the server response.
 */
public enum MessageType {
  // Sent by server to clients
  PING,
  // Reply to ping from clients
  PONG,

  // Nickname selection
  NICKNAME_REQUEST,
  // Server response
  NICKNAME_OK,

  // Lobby actions
  CREATE_GAME,
  JOIN_GAME,
  SELECT_TOWER,
  // Lobby data
  GAMEINFO,

  // Game actions
  START_GAME,
  PLAY_ACTION,
  INIT_RECONNECTED, // start game in case of reconnection
  // Game data
  GAMEDATA,

  // Player disconnection management,
  PLAYER_DISCONNECTED,
  PLAYER_RECONNECTED,

  ERROR,

  // Used internally for local network-related events, these must never be sent over the network
  INTERNAL_SOCKET_ERROR,
}
