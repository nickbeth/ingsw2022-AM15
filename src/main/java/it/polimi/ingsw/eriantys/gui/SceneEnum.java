package it.polimi.ingsw.eriantys.gui;

public enum SceneEnum {
  MENU("MainMenu.fxml"),
  RULES("Rules.fxml"),
  CONNECTION("Connection.fxml");

  public final String path;

  SceneEnum(String path) {
    this.path = path;
  }
}
