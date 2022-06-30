package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.beans.PropertyChangeEvent;

public class MainMenuController extends FXMLController {
  /**
   * Sets scene to Connection
   */
  @FXML
  private void playButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.CONNECTION);
  }

  /**
   * Sets scene to Rules
   */
  @FXML
  private void rulesButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.RULES);
  }

  /**
   * closes the application
   */
  @FXML
  private void quitButtonAction(ActionEvent actionEvent) {
    gui.closeApplication();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }
}
