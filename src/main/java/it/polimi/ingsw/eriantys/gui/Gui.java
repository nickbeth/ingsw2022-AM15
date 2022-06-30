package it.polimi.ingsw.eriantys.gui;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import static it.polimi.ingsw.eriantys.controller.EventType.INTERNAL_SOCKET_ERROR;

public class Gui extends Application implements PropertyChangeListener {
  private Stage stage;
  private SceneEnum currScene;
  private FXMLController controller;

  /**
   * the start method is called at the beggining of the application life cycle,
   * this method calls ,setups the stage and sets the first scene
   */
  @Override
  public void start(Stage primaryStage) {
    Controller.get().addListener(this, INTERNAL_SOCKET_ERROR.tag);
    stage = primaryStage;
    stage.setTitle("Eriantys");
    stage.getIcons().add(new Image("/assets/misc/game-icon.png"));
    // Pressing on the red X to close a stage will call the closeApplication method
    stage.setOnCloseRequest(e -> closeApplication());
    currScene = SceneEnum.MENU;
    setScene(currScene);
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Closes the current stage, and removes this as listener for any event
   */
  public void closeApplication() {
    Controller.get().removeListener(this, INTERNAL_SOCKET_ERROR.tag);
    stage.close();
  }

  /**
   * Displays an alert with the text of the received error.
   *
   * @param error displayed error text
   */
  public static void showError(String error) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(error);
    alert.showAndWait();
  }

  /**
   * Displays an alert notifying a socket error
   */
  public void showSocketError() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText("a connection error has occured: disconnected from server");
    alert.setOnCloseRequest(e -> closeApplication());
    alert.showAndWait();
  }

  /**
   * Calls finish on old scene controller, loads the next scene, calls start and updateAll on the next scene controller
   */
  public void setScene(SceneEnum scene) {
    if (controller != null)
      controller.finish();

    FXMLLoader loader = new FXMLLoader();
    Parent root;
    URL url = getClass().getResource(scene.path);
    try {
      loader.setLocation(url);
      root = loader.load();
      controller = loader.getController();
      controller.setGui(this);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    currScene = scene;
    controller.start();
    controller.updateAll();
    // unminimizing the stage to prevent resize problems
    stage.setIconified(false);
    stage.setScene(new Scene(root));
    stage.sizeToScene();
    stage.show();
  }

  /**
   * If for any reason an internal socket error happens the gui returns to the Main menu scene
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    setScene(SceneEnum.MENU);
  }
}
