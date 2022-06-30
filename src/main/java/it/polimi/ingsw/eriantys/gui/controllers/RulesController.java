package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.util.Objects;

public class RulesController extends FXMLController {
  @FXML
  private ImageView view;
  @FXML
  private Button nextPage;
  @FXML
  private Button previousPage;
  private int currPage = 1;
  
  /**
   * Sets scene to Menu
   */
  @FXML
  private void backButtonAction(ActionEvent actionEvent) {
    gui.setScene(SceneEnum.MENU);
  }

  @FXML
  public void goNextPage(ActionEvent actionEvent) {
    nextPage.setVisible(currPage < 6);
    currPage++;
    previousPage.setVisible(true);
    view.setImage(getImage());
  }
  
  @FXML
  private void goPreviousPage(ActionEvent actionEvent) {
    previousPage.setVisible(currPage > 2);
    currPage--;
    nextPage.setVisible(true);
    view.setImage(getImage());
  }
  
  private Image getImage() {
    InputStream stream = getClass().getResourceAsStream("/assets/rules/Eriantys_rules-" + currPage + ".png");
    return new Image(Objects.requireNonNull(stream));
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }
}
