package it.polimi.ingsw.eriantys.gui;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.FXMLController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;


public class Gui extends Application {
  private static Controller controller;
  private Stage stage;
  private SceneEnum prevScene;
  private SceneEnum currScene;
  private EnumMap<SceneEnum, Scene> sceneMap = new EnumMap<>(SceneEnum.class);
  private EnumMap<SceneEnum, FXMLController> controllerMap = new EnumMap<>(SceneEnum.class);

  public static void showError(String error){
    //TODO new Stage pop up window for errors
  }

  public static void setController(Controller contr) {
    controller = contr;
  }

  public Controller getController() {
    return controller;
  }

  /**
   * the start method is called at the beggining of the application life cycle,
   * this method calls {@link #initializeScenes()},setups the stage and sets the first scene
   */
  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    initializeScenes();
    //pressing on the red X to close a stage will call the closeApplication method
    stage.setOnCloseRequest(e -> closeApplication());
    currScene = SceneEnum.MENU;
    stage.setScene(sceneMap.get(currScene));
    controller.getActionInvoker().addListener(controllerMap.get(currScene));
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
  public void closeApplication(){
    stage.close();
  }

  /**
   * sets a new scene, updates the next controller, and updates the listener list in action invoker.
   * @param scene
   */
  public void setScene(SceneEnum scene){
    prevScene = currScene;
    currScene = scene;
    FXMLController nextController = controllerMap.get(currScene);
    nextController.updateAll();
    controller.getActionInvoker().removeListener(controllerMap.get(prevScene));
    controller.getActionInvoker().addListener(nextController);
    stage.setScene(sceneMap.get(currScene));
    stage.show();
  }

}
