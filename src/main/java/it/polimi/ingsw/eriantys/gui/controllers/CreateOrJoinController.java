package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateOrJoinController extends FXMLController {
  @FXML
  private Label errorMessage;
  @FXML
  private ChoiceBox<Integer> playerNumberChoice;
  @FXML
  private ChoiceBox<GameMode> gameModeChoice;
  @FXML
  private Group joinGameGroup;
  @FXML
  private Group createGameGroup;
  @FXML
  private TextField gameUidField;


  @FXML
  private void continueCreateGame(ActionEvent actionEvent) {
    Integer playerNumber = playerNumberChoice.getValue();
    GameMode gameMode = gameModeChoice.getValue();

    if (gameMode == null) {
      errorMessage.setText("please select a game mode to create the game");
      errorMessage.setVisible(true);
    }
    else if (playerNumber == null) {
      errorMessage.setText("please select a number of players to create the game");
      errorMessage.setVisible(true);
    }
    else
      gui.getController().sendCreateGame(playerNumber, gameMode);
  }

  @FXML
  private void continueJoinGame(ActionEvent actionEvent) {
    String gameCode = gameUidField.getText();
    if(!gameCode.isEmpty())
      gui.getController().sendJoinGame(gameCode);
    else {
      errorMessage.setText("please insert a gameCode to start the game");
      errorMessage.setVisible(true);
    }
  }

  @FXML
  private void createGame(ActionEvent actionEvent) {
    errorMessage.setVisible(false);
    errorMessage.setLayoutX(209);
    errorMessage.setLayoutY(128);
    createGameGroup.setVisible(true);
    joinGameGroup.setVisible(false);
  }

  @FXML
  private void joinGame(ActionEvent actionEvent) {
    errorMessage.setVisible(false);
    errorMessage.setLayoutX(209);
    errorMessage.setLayoutY(180);
    createGameGroup.setVisible(false);
    joinGameGroup.setVisible(true);
  }

  @FXML
  private void backButton(ActionEvent actionEvent) {
    //TODO: handle disconnection from server with the press of the back button
    gui.setScene(SceneEnum.CONNECTION);
  }
}