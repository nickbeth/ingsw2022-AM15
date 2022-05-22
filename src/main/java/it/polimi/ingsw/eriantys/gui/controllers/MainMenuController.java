package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController implements FXMLController {
  @FXML private Button playButton;
  @FXML private Button rulesButton;
  @FXML private Button quitButton;
  private Gui gui;

  @FXML
  private void playButtonAction(ActionEvent actionEvent) {
    System.out.println("start");
  }

  @FXML
  private void rulesButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.RULES);
  }

  @FXML
  private void quitButtonAction(ActionEvent actionEvent) {
    gui.closeApplication();
  }


  @Override
  public void setGui(Gui gui) {
    this.gui = gui;
  }
}
