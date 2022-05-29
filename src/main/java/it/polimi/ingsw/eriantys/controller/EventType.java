package it.polimi.ingsw.eriantys.controller;

public enum EventType {
  ERROR("network_error"),
  GAMEDATA_EVENT("gamedata"),
  GAMEINFO_EVENT("gameinfo"),
  INTERNAL_SOCKET_ERROR("internal_socket_error"),
  NICKNAME_OK("nickname_ok"),
  PLAYER_CONNECTION_CHANGED("player_connection_changed"),
  GAME_ENDED("game_ended");

  public String tag;

  EventType(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return tag;
  }
}
