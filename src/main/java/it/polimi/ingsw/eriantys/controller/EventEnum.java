package it.polimi.ingsw.eriantys.controller;

public enum EventEnum {
  GAMEINFO_EVENT("gameinfo"),
  GAMEDATA_EVENT("gamedata"),
  NICKNAME_OK_EVENT("ok");

  public String tag;

  EventEnum(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return tag;
  }
}
