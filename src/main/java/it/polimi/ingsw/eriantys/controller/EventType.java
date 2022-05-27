package it.polimi.ingsw.eriantys.controller;

public enum EventType {
  GAMEINFO_EVENT("gameinfo"),
  GAMEDATA_EVENT("gamedata"),
  NICKNAME_OK("nickname_ok"),
  PLAYER_CONNECTION_CHANGED("player_connection_changed"),
  SOCKET_ERROR("socket_error"),
  ERROR("network_error");

  public String tag;

  EventType(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return tag;
  }
}
