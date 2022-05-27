package it.polimi.ingsw.eriantys.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.beans.PropertyChangeEvent;

public class PlanningSceneController extends FXMLController{
  @FXML
  private Group assistantCards;
  @FXML
  private Button playCardButton;

  @FXML
  private void showAssistCards(ActionEvent actionEvent) {
    assistantCards.setVisible(true);
  }

  @FXML
  private void quitGameAction(ActionEvent actionEvent) {
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }

  @Override
  public void updateAll() {
    super.updateAll();
  }
}
