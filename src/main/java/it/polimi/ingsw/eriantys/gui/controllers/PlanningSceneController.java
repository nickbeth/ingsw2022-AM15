package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.List;

public class PlanningSceneController extends FXMLController {

  @FXML
  private GridPane otherPlayersGrid;
  @FXML
  private StackPane playerTwo;
  @FXML
  private StackPane playerFour;
  @FXML
  private StackPane playerThree;
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
  private void selDashboardTwo(MouseEvent mouseEvent) {
    Dashboard dashboard = gui.getController().getGameState().getPlayers().get(1).getDashboard();

  }

  @FXML
  private void selDashboardThree(MouseEvent mouseEvent) {
    ImageView source = (ImageView) mouseEvent.getSource();
  }

  @FXML
  private void selDashboardFour(MouseEvent mouseEvent) {
    ImageView source = (ImageView) mouseEvent.getSource();

  }

  private void showDashboard(Dashboard dashboard) {

  }

  @FXML
  private void quitGameAction(ActionEvent actionEvent) {
  }

  @FXML
  private void playAssistCardAction(MouseEvent mouseEvent) {
    ImageView img = (ImageView) mouseEvent.getSource();
    Logger.debug("Played assistant card " + img.getId());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }

  @Override
  public void updateAll() {
    super.updateAll();
    updatePlayers();
  }


  private void updateDashboard() {

  }

  private void updatePlayers() {
    List<Player> players = gui.getController().getGameState().getPlayers();
    for (int i = 0; i < players.size(); i++) {
      ImageView img = new ImageView(new Image("/assets/wizards/wizard-" + i + ".jpg"));
      img.setFitWidth(40);
      img.setPreserveRatio(true);
      img.setId("wizardicon");
      Label nickname = new Label(players.get(i).getNickname());
      nickname.setAlignment(Pos.TOP_LEFT);
      nickname.setId("playerNickname");
      //TODO: add show showDashboard action on click to image, maybe it depends on the grid?
      otherPlayersGrid.add(img, 0, i);
      otherPlayersGrid.add(nickname, 1, i);
    }
  }

  private void updateAssistCards() {
    ArrayList<AssistantCard> cards = gui.getController().getGameState().getPlayer(gui.getController().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.value + ".png");
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
