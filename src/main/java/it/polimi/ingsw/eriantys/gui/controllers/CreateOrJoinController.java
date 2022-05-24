package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.GAMEINFO_EVENT;

public class CreateOrJoinController extends FXMLController implements PropertyChangeListener {
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


  /**
   * Sends a createGame Message <br>
   * - If the choice boxes are empty displays an error message.
   */
  @FXML
  private void continueCreateGame(ActionEvent actionEvent) {
    Integer playerNumber = playerNumberChoice.getValue();
    GameMode gameMode = gameModeChoice.getValue();

    if (gameMode == null) {
      errorMessage.setText("please select a game mode to create the game");
      errorMessage.setVisible(true);
    } else if (playerNumber == null) {
      errorMessage.setText("please select a number of players to create the game");
      errorMessage.setVisible(true);
    } else
      gui.getController().sender().sendCreateGame(playerNumber, gameMode);
  }

  /**
   * Sends a joinGame Message <br>
   * - If the gameCode field is empty displays an error message.
   */
  @FXML
  private void continueJoinGame(ActionEvent actionEvent) {
    String gameCode = gameUidField.getText();
    if (!gameCode.isEmpty())
      gui.getController().sender().sendJoinGame(gameCode);
    else {
      errorMessage.setText("please insert a gameCode to start the game");
      errorMessage.setVisible(true);
    }
  }

  /**
   * Displays createGame controls, hides joinGame controls
   */
  @FXML
  private void createGame(ActionEvent actionEvent) {
    errorMessage.setVisible(false);
    errorMessage.setLayoutX(209);
    errorMessage.setLayoutY(128);
    createGameGroup.setVisible(true);
    joinGameGroup.setVisible(false);
  }

  /**
   * Displays joinGame controls, hides createGame controls
   */
  @FXML
  private void joinGame(ActionEvent actionEvent) {
    errorMessage.setVisible(false);
    errorMessage.setLayoutX(209);
    errorMessage.setLayoutY(180);
    createGameGroup.setVisible(false);
    joinGameGroup.setVisible(true);
  }

  /**
   * Returns to Connection scene, handles disconnection from server
   */
  @FXML
  private void backButton(ActionEvent actionEvent) {
    //TODO: handle disconnection from server with the press of the back button
    gui.setScene(SceneEnum.CONNECTION);
  }

  /**
   * Sets scene to lobby scene
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    gui.setScene(SceneEnum.LOBBY);
  }

  @Override
  public void start() {
    super.start();
    gui.getController().addListener(this, GAMEINFO_EVENT.tag);
  }

  @Override
  public void finish() {
    super.finish();
    gui.getController().removeListener(this, GAMEINFO_EVENT.tag);
  }
}
