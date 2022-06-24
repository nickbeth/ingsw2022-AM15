package it.polimi.ingsw.eriantys.gui;

import it.polimi.ingsw.eriantys.gui.controllers.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;


public class Gui extends Application {
  private Stage stage;
  private SceneEnum currScene;
  private FXMLController controller;
  private EnumMap<SceneEnum, Scene> sceneMap = new EnumMap<>(SceneEnum.class);
  private EnumMap<SceneEnum, FXMLController> controllerMap = new EnumMap<>(SceneEnum.class);

  /**
   * the start method is called at the beggining of the application life cycle,
   * this method calls ,setups the stage and sets the first scene
   */
  @Override
  public void start(Stage primaryStage) {
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
   * closes the current stage
   */
  public void closeApplication() {
    stage.close();
  }

  /**
   * Displays an alert with the text of the received error.
   *
   * @param error
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
   * Calls finish on old scene controller, loads the next scene, calls start and updatAll on the next scene controller
   *
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
      controllerMap.put(scene, controller);
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
}
