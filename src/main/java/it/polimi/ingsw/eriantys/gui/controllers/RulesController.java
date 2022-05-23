package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.beans.PropertyChangeEvent;

public class RulesController extends FXMLController {
  @FXML
  private Button backButton;

  /**
   * Sets scene to Menu
   */
  @FXML
  public void backButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.MENU);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }

}
