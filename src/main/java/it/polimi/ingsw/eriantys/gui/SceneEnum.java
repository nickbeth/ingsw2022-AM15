package it.polimi.ingsw.eriantys.gui;

public enum SceneEnum {
  MENU("MainMenu.fxml"),
  RULES("Rules.fxml");

  public final String path;

  SceneEnum(String path) {
    this.path = path;
  }
}
