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
  private SceneEnum prevScene;
  private SceneEnum currScene;
  private EnumMap<SceneEnum, Scene> sceneMap = new EnumMap<>(SceneEnum.class);
  private EnumMap<SceneEnum, FXMLController> controllerMap = new EnumMap<>(SceneEnum.class);

  /**
   * the start method is called at the beggining of the application life cycle,
   * this method calls {@link #initializeScenes()},setups the stage and sets the first scene
   */
  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    stage.setTitle("Eriantys");
    stage.getIcons().add(new Image("/assets/misc/game-icon.png"));
    initializeScenes();
    // Pressing on the red X to close a stage will call the closeApplication method
    stage.setOnCloseRequest(e -> closeApplication());
    currScene = SceneEnum.MENU;
    stage.setScene(sceneMap.get(currScene));
    stage.setResizable(false);
    stage.show();
  }

  /**
   * This method initalizes the sceneMap and controllerMap by loading them from the .fxml files
   * it also passes a reference of the gui to the controller.
   */
  private void initializeScenes() {
    for (SceneEnum scene : SceneEnum.values()) {
      FXMLLoader loader = new FXMLLoader();
      URL url = getClass().getResource("/fxml/" + scene.path);
      try {
        loader.setLocation(url);
        Parent root = loader.load();
        FXMLController controller = loader.getController();
        controller.setGui(this);
        controllerMap.put(scene, controller);
        sceneMap.put(scene, new Scene(root));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
    alert.showAndWait();
  }

  /**
   * sets a new scene, updates the next controller, and updates the listener list in action invoker.
   *
   * @param scene
   * @param eventTag
   */
  public void setScene(SceneEnum scene) {
    prevScene = currScene;
    currScene = scene;
    controllerMap.get(prevScene).finish();
    FXMLController nextController = controllerMap.get(currScene);
    nextController.start();
    nextController.updateAll();
    // Showing unminimizing the stage to prevent resize problems
    stage.setIconified(false);
    stage.setScene(sceneMap.get(currScene));
    stage.sizeToScene();
    stage.show();
  }
}
