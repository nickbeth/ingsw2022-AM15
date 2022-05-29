package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.controllers.utils.IslandPattern;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
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
import javafx.scene.layout.*;
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
  private GridPane islandsGrid;
  @FXML
  private AnchorPane rootPane;
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

  private EnumMap<TowerColor, String> towerColorToPath = new EnumMap<TowerColor, String>(TowerColor.class);
  private EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);
  private EnumMap<HouseColor, String> professorColorToPath = new EnumMap<>(HouseColor.class);
  private AnchorPane[][] islands = new AnchorPane[5][4];


  @FXML
  private void showAssistCards(ActionEvent actionEvent) {
    updateAssistCards();
    assistCardPanel.setVisible(true);
    otherPlayersGrid.getClip();
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
    createIslandsGrid();
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

      ImageView towerIcon = new ImageView(new Image("/assets/realm/tower-" + players.get(i).getColorTeam() + ".png"));
      towerIcon.setFitWidth(20);
      towerIcon.setPreserveRatio(true);
      Label towerAmount = new Label("x" + players.get(i).getDashboard().getTowers().count, towerIcon);
      otherPlayersGrid.add(towerAmount, 2, i);
    }
  }


  //TODO: redo with tilepane or girdpane
  private void updateAssistCards() {
    assistCards.getChildren().clear();
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

  //**

  private void createIslandsGrid() {
    List<Island> islands = gui.getController().getGameState().getPlayingField().getIslands();
    IslandPattern pattern = IslandPattern.getPattern(islands.size());
    int[][] matrix = pattern.matrix;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] != -1) {
          islandsGrid.add(createIslandPane(islands.get(matrix[i][j]), matrix[i][j]), i, j);
        }
      }
    }
  }

  private AnchorPane createIslandPane(Island island, int islandIndex) {
    AnchorPane islandPane = new AnchorPane();
    ArrayList<Label> students = new ArrayList<>();
    islandPane.setPrefWidth(200);
    islandPane.setPrefHeight(150);

    ImageView islandImg = new ImageView(new Image("/assets/realm/island-" + (islandIndex % 3 + 1) + ".png"));
    islandImg.setFitWidth(200);
    islandImg.setFitHeight(150);
    islandImg.setPreserveRatio(true);
    islandPane.getChildren().add(islandImg);

    ImageView red = new ImageView(new Image(studentColorToPath.get(HouseColor.RED)));
    ImageView blue = new ImageView(new Image(studentColorToPath.get(HouseColor.BLUE)));
    ImageView green = new ImageView(new Image(studentColorToPath.get(HouseColor.GREEN)));
    ImageView yellow = new ImageView(new Image(studentColorToPath.get(HouseColor.YELLOW)));
    ImageView pink = new ImageView(new Image(studentColorToPath.get(HouseColor.PINK)));
    red.setFitWidth(20);
    red.setPreserveRatio(true);
    blue.setFitWidth(20);
    blue.setPreserveRatio(true);
    green.setFitWidth(20);
    green.setPreserveRatio(true);
    yellow.setFitWidth(20);
    yellow.setPreserveRatio(true);
    pink.setFitWidth(20);
    pink.setPreserveRatio(true);

    Label redStudent = new Label("x" + island.getStudents().getCount(HouseColor.RED), red);
    islandPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 30.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    Label pinkStudent = new Label("x" + island.getStudents().getCount(HouseColor.PINK), pink);
    islandPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 30.0);
    AnchorPane.setLeftAnchor(pinkStudent, 115.0);
    Label greenStudent = new Label("x" + island.getStudents().getCount(HouseColor.GREEN), green);
    islandPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    Label blueStudent = new Label("x" + island.getStudents().getCount(HouseColor.BLUE), blue);
    islandPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);
    AnchorPane.setLeftAnchor(blueStudent, 115.0);
    Label yellowStudent = new Label("x" + island.getStudents().getCount(HouseColor.YELLOW), yellow);
    islandPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 30.0);
    AnchorPane.setLeftAnchor(yellowStudent, 75.0);

    if (island.getTowerColor().isPresent()) {
      ImageView tower = new ImageView(new Image(towerColorToPath.get(island.getTowerColor().get())));
      tower.setFitWidth(20);
      tower.setPreserveRatio(true);
      Label towerCount = new Label("x" + island.getTowerCount(), tower);
      islandPane.getChildren().add(towerCount);
      AnchorPane.setBottomAnchor(towerCount, 75.0);
      AnchorPane.setLeftAnchor(towerCount, 75.0);
    }

    if (island.isLocked()) {
      ImageView lock = new ImageView(new Image("/assets/realm/lock-icon.png"));
      lock.setFitWidth(20);
      lock.setPreserveRatio(true);
      islandPane.getChildren().add(lock);
      AnchorPane.setBottomAnchor(lock, 50.0);
      AnchorPane.setLeftAnchor(lock, 63.0);
    }

    return islandPane;
  }

  @Override
  public void start() {
    super.start();
    gui.getController().addListener(this, GAMEDATA_EVENT.tag);

    //initializing path maps
    for (TowerColor color : TowerColor.values()) {
      towerColorToPath.put(color, "/assets/realm/tower-" + color + ".png");
    }

    for (HouseColor color : HouseColor.values()) {
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");
    }

    for (HouseColor color : HouseColor.values()) {
      professorColorToPath.put(color, "/assets/realm/professor-" + color + ".png");
    }
  }

  @Override
  public void finish() {
    super.finish();
    gui.getController().removeListener(this, GAMEDATA_EVENT.tag);
  }
}
