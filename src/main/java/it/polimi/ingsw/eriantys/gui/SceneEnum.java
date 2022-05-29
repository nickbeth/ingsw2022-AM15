package it.polimi.ingsw.eriantys.gui;

public enum SceneEnum {
  MENU("MainMenu.fxml"),
  CONNECTION("Connection.fxml"),
  CREATE_OR_JOIN("CreateOrJoin.fxml"),
  LOBBY("Lobby.fxml"),
  RULES("Rules.fxml"),
  PLANNING("PlanningScene.fxml");

  public final String path;

  SceneEnum(String path) {
    this.path = path;
  }
}
