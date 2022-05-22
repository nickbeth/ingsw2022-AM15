package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.tinylog.Logger;

public class RulesController extends FXMLController {
  @FXML
  private Button backButton;

  @FXML
  public void backButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.MENU);
  }

}
