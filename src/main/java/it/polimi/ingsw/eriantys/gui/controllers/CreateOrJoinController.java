package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class CreateOrJoinController extends FXMLController {
  @FXML
  private ChoiceBox playerNumberChoice;
  @FXML
  private ChoiceBox gameModeChoice;
  @FXML
  private Group joinGameGroup;
  @FXML
  private Group createGameGroup;
  @FXML
  public TextField gameUidField;


  @FXML
  private void continueCreateGame(ActionEvent actionEvent) {
    String playerNumberStr = (String) playerNumberChoice.getValue();
    int playerNumber;
    GameMode gameMode = (GameMode) gameModeChoice.getValue();
    try {
      playerNumber = Integer.parseInt(playerNumberStr);
      gui.getController().sendCreateGame(playerNumber, gameMode);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void continueJoinGame(ActionEvent actionEvent) {
  }

  @FXML
  private void createGame(ActionEvent actionEvent) {
    createGameGroup.setVisible(true);
    joinGameGroup.setVisible(false);
  }

  @FXML
  private void joinGame(ActionEvent actionEvent) {
    createGameGroup.setVisible(false);
    joinGameGroup.setVisible(true);
  }

  @FXML
  public void initialize() {
  }
}
