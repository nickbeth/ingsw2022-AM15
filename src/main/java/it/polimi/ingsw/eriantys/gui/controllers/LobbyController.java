package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.GameInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.beans.PropertyChangeEvent;

public class LobbyController extends FXMLController {
  @FXML
  private Label gameMode;
  @FXML
  private Label maxPlayerCount;

  @FXML
  private void startGameAction(ActionEvent actionEvent) {
  }

  @FXML
  public void changeColorAction(ActionEvent actionEvent) {
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    updateAll();
  }

  @Override
  public void updateAll() {
    GameInfo gameInfo = gui.getController().getGameInfo();
    gameMode.setText(gameInfo.getMode().toString());
    maxPlayerCount.setText(String.valueOf(gameInfo.getMaxPlayerCount()));
  }
}
