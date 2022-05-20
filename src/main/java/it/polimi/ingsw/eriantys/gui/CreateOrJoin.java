package it.polimi.ingsw.eriantys.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class CreateOrJoin extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    HBox root = new HBox();
    Scene scene = new Scene(root, 200, 200);
    Button joinGame = new Button("join game");
    Button createGame = new Button("create game");


    root.getChildren().add(joinGame);
    root.getChildren().add(createGame);
    stage.setScene(scene);
    stage.setTitle("CreateOrJoin");
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
