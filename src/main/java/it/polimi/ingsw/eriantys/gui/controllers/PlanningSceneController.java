package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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
import java.util.EnumMap;
import java.util.List;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;

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

  private EnumMap<TowerColor, String> towerPath = new EnumMap<TowerColor, String>(TowerColor.class);

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
    updatePlayerGrid();
  }


  private void updateDashboard() {

  }

  private void updatePlayerGrid() {
    otherPlayersGrid.getStyleClass().add("grid-players");
    List<Player> players = gui.getController().getGameState().getPlayers();
    for (int i = 0; i < players.size(); i++) {
      ImageView wizardIcon = new ImageView(new Image("/assets/wizards/wizard-" + i + ".jpg"));
      wizardIcon.setFitWidth(40);
      wizardIcon.setPreserveRatio(true);
      wizardIcon.getStyleClass().add("image-wizard");
      Label nickname = new Label(players.get(i).getNickname(), wizardIcon);
      nickname.getStyleClass().add("label-nicknames");
      nickname.setContentDisplay(ContentDisplay.RIGHT);
      //TODO: add show showDashboard action on click to image, maybe it depends on the grid?
      otherPlayersGrid.add(nickname, 0, i);
      GridPane.setHalignment(nickname, HPos.RIGHT);

      ImageView cardIcon = new ImageView(new Image("/assets/misc/card-icon.png"));
      cardIcon.setFitWidth(20);
      cardIcon.setPreserveRatio(true);
      Label cardAmount = new Label("x" + players.get(i).getCards().size(), cardIcon);
      cardAmount.getStyleClass().add("label-cardamount");
      otherPlayersGrid.add(cardAmount, 1, i);

      ImageView towerIcon = new ImageView(new Image("/assets/realm/tower-" + players.get(i).getColorTeam() +".png"));
      towerIcon.setFitWidth(20);
      towerIcon.setPreserveRatio(true);
      Label towerAmount = new Label("x" + players.get(i).getDashboard().getTowers().count, towerIcon);
      otherPlayersGrid.add(towerAmount, 2, i);
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

  @Override
  public void start() {
    super.start();
    gui.getController().addListener(this, GAMEDATA_EVENT.tag);
  }

  @Override
  public void finish() {
    super.finish();
    gui.getController().removeListener(this, GAMEDATA_EVENT.tag);
  }
}
