package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.controller.EventType.NICKNAME_OK;

public class ConnectionController extends FXMLController {
  @FXML
  private TextField nicknameField;
  @FXML
  private Button backButton;
  @FXML
  private Button confirm;
  @FXML
  private Label errorMessage;
  @FXML
  private TextField serverIpField;
  @FXML
  private TextField serverPortField;

  @FXML
  private void backButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.MENU);
  }

  /**
   * Tries to create a connection to the game server <br>
   * - if the parameters are invalid it displays text in the errorMessage label
   */
  @FXML
  private void confirmButtonAction(ActionEvent actionEvent) {
    String ipStr = serverIpField.getText();
//    String ipStr = "192.168.75.197";
    String portStr = serverPortField.getText();
    int port = Client.DEFAULT_PORT;
    try {
      if (!portStr.isEmpty()) {
        port = Integer.parseInt(portStr);
      }
      if (!Controller.getController().connect(ipStr, port)) {
        errorMessage.setText("Failed to connect to the server");
        errorMessage.setOpacity(1);
      }

    } catch (NumberFormatException e) {
      errorMessage.setText("Invalid port, try again.");
      errorMessage.setOpacity(1);
    }
    Controller.getController().sender().sendNickname(nicknameField.getText());
  }

  @Override
  public void start() {
    super.start();
    Controller.getController().addListener(this, NICKNAME_OK.tag);
  }

  @Override
  public void finish() {
    super.finish();
    Controller.getController().removeListener(this, NICKNAME_OK.tag);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    gui.setScene(SceneEnum.CREATE_OR_JOIN);
  }

}
