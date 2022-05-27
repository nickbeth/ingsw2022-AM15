package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ArrayList;

public class PlanningSceneController extends FXMLController {
  @FXML
  private TilePane assistCards;
  @FXML
  private StackPane assistCardPanel;
  @FXML
  private Button playCardButton;

  @FXML
  private void showAssistCards(ActionEvent actionEvent) {
    updateAssistCards();
    assistCardPanel.setVisible(true);
  }

  @FXML
  private void hideAssistCards(MouseEvent mouseEvent) {
    assistCardPanel.setVisible(false);
  }

  @FXML
  private void quitGameAction(ActionEvent actionEvent) {
  }

  private void playAssistCardAction(MouseEvent mouseEvent) {
    ImageView img =(ImageView)mouseEvent.getSource();
    Logger.debug("Played assistant card " + img.getId());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }

  @Override
  public void updateAll() {
    super.updateAll();
  }

  private void updateAssistCards() {
    ArrayList<AssistantCard> cards = gui.getController().getGameState().getPlayer(gui.getController().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_"+ card.value + ".png");
      img.setImage(new Image(String.valueOf(imgPath)));
      img.setFitWidth(160);
      img.setId(card.toString());
      img.setPreserveRatio(true);
      img.setOnMouseClicked(this::playAssistCardAction);
      //TODO: set highlight or zoom on mouse hover
      assistCards.getChildren().add(img);
    });
    /*assistCards.getChildren().add(new)*/
  }
}
