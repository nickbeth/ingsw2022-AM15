package it.polimi.ingsw.eriantys.gui;

public enum SceneEnum {
  MENU("/fxml/MainMenu.fxml"),
  CONNECTION("/fxml/Connection.fxml"),
  CREATE_OR_JOIN("/fxml/CreateOrJoin.fxml"),
  LOBBY("/fxml/Lobby.fxml"),
  RULES("/fxml/Rules.fxml"),
  GAME("/fxml/GameScene.fxml");

  public final String path;

  SceneEnum(String path) {
    this.path = path;
  }
}
