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
   *
   */
  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    initializeScenes();
    //pressing on the red X ti close a stage will call the closeApplication method
    stage.setOnCloseRequest(e -> closeApplication());
    setScene(SceneEnum.MENU);
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
        Logger.warn(scene + " scene path not found");
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
   * sets a new scene and updates the listener list in action invoker
   * @param scene
   */
  public void setScene(SceneEnum scene){
    controller.getActionInvoker().removeAllListener();
    controller.getActionInvoker().addListener(controllerMap.get(scene));
    stage.setScene(sceneMap.get(scene));
    stage.show();
  }


}
