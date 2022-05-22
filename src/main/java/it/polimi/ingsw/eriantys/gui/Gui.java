package it.polimi.ingsw.eriantys.gui;

import it.polimi.ingsw.eriantys.gui.controllers.FXMLController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;


public class Gui extends Application {
  private Stage stage;
  private EnumMap<SceneEnum, Scene> sceneMap = new EnumMap<>(SceneEnum.class);

  /**
   *
   */
  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    initializeScenes();
    //pressing on the red X ti close a stage will call the closeApplication method
    stage.setOnCloseRequest(e -> closeApplication());
    stage.setScene(sceneMap.get(SceneEnum.MENU));
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  /**
   * This method initalizes the sceneMap by loading scenes from the .fxml files and passing the gui to the controller
   */
  private void initializeScenes() {
    for (SceneEnum scene : SceneEnum.values()) {
      FXMLLoader loader = new FXMLLoader();
      URL url = getClass().getResource("/fxml/" + scene.path);
      try {
        loader.setLocation(url);
        Parent root = loader.load();
        ((FXMLController) loader.getController()).setGui(this);
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

  public void setScene(SceneEnum scene){
    stage.setScene(sceneMap.get(scene));
    stage.show();
  }


}
