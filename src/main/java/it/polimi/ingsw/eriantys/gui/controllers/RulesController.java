package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.tinylog.Logger;

public class RulesController implements FXMLController {

  @FXML
  private Button backButton;
  private Gui gui;

  @FXML
  public void backButtonAction(ActionEvent actionEvent) {
    Logger.debug("back");
    gui.setScene(SceneEnum.MENU);
  }

  @Override
  public void setGui(Gui gui) {
    this.gui = gui;
  }
}
