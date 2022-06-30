package it.polimi.ingsw.eriantys.controller;

public enum EventType {
  NICKNAME_OK("nickname_ok"),
  GAMELIST("game_list"),

  START_GAME("start_game"),

  GAMEINFO_EVENT("gameinfo"),
  GAMEDATA_EVENT("gamedata"),
  DELIBERATE_DISCONNECTION("deliberate_disconnection"),

  INTERNAL_SOCKET_ERROR("internal_socket_error"),
  ERROR("network_error"),

  PLAYER_CONNECTION_CHANGED("player_connection_changed"),

  END_GAME("end_game"),

  INPUT_ENTERED("input_entered");

  public final String tag;

  EventType(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return tag;
  }
}
