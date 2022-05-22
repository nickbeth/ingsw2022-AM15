package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.tinylog.Logger;

public class ConnectionController extends FXMLController{
  public Button backButton;
  public Button confirm;
  @FXML private TextField serverIp;
  @FXML private TextField serverPort;
  
  @FXML
  private void backButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.MENU);
  }
  
  @FXML
  private void confirmButtonAction(ActionEvent actionEvent) {
    Logger.info("Confirm Action event");
  }
}
