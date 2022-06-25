package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.START_GAME;


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
      Controller.get().sender().sendCreateGame(playerNumber, gameMode);
  }

  /**
   * Sends a joinGame Message <br>
   * - If the gameCode field is empty displays an error message.
   */
  @FXML
  private void continueJoinGame(ActionEvent actionEvent) {
    try {
      GameCode gameCode = GameCode.parseCode(gameUidField.getText());
      Controller.get().sender().sendJoinGame(gameCode);
    } catch (GameCode.GameCodeException e) {
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
    Controller.get().sender().sendQuitGame();
    Controller.get().disconnect();
    gui.setScene(SceneEnum.CONNECTION);
  }

  /**
   * Sets default values for choice boxes, and gameID text field
   */
  private void setDefaultValues(){
    playerNumberChoice.setValue(2);
    gameModeChoice.setValue(GameMode.NORMAL);
    gameUidField.setText("AAAA");
  }

  /**
   * Sets scene to lobby scene
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(GAMEINFO_EVENT.tag))
      gui.setScene(SceneEnum.LOBBY);
    else {
      gui.setScene(SceneEnum.GAME);
    }
  }

  @Override
  public void start() {
    super.start();
    setDefaultValues();
    Controller.get().addListener(this, GAMEINFO_EVENT.tag);
    Controller.get().addListener(this, START_GAME.tag);
  }

  @Override
  public void finish() {
    super.finish();
    Controller.get().removeListener(this, GAMEINFO_EVENT.tag);
    Controller.get().removeListener(this, START_GAME.tag);
  }
}
