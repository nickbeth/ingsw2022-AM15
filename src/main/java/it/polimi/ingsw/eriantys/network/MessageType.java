package it.polimi.ingsw.eriantys.network;

/**
 * This class enumerates all the possible message types.
 *
 * The commands are grouped by game phase (lobby, playing),
 * and are further divided into client commands followed by the type of the server response.
 */
public enum MessageType {
  PING,
  PONG,

  // Lobby actions
  CREATE_GAME,
  JOIN_GAME,
  SELECT_TOWER,
  // Lobby data
  GAMEINFO,

  // Game actions
  //INITIALIZE_GAME,
  START_GAME,
  PLAY_ACTION,
  // Game data
  GAMEDATA,

  ERROR,
}
