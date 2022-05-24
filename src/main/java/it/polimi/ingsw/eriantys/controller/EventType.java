package it.polimi.ingsw.eriantys.controller;

public enum EventType {
  GAMEINFO_EVENT("gameinfo"),
  GAMEDATA_EVENT("gamedata"),
  NICKNAME_OK("nickname_ok"),
  SOCKET_ERROR("socket_error"),
  NETWORK_ERROR("network_error");

  public String tag;

  EventType(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return tag;
  }
}
