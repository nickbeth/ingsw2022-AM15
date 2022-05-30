package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.utils.IslandPattern;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
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

public class PlanningSceneController extends FXMLController {
  @FXML
  private TilePane dashboardTowers;
  @FXML
  private GridPane profTableGrid;
  @FXML
  private GridPane entranceGrid;
  @FXML
  private AnchorPane dashboardClient;
  @FXML
  private GridPane studentHallGrid;
  @FXML
  private VBox cloudBox;
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
    Dashboard dashboard = Controller.getController().getGameState().getPlayers().get(1).getDashboard();

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
    updateDashboard();
    createIslandsGrid();
    createCloudGrid();
  }


  private void updateDashboard() {
    Player player = Controller.getController().getGameState().getPlayer(Controller.getController().getNickname());
    //updating dining hall
    studentHallGrid.getChildren().clear();
    Students diningHall = player.getDashboard().getDiningHall();

    //updating GREEN table
    for (int i = 0; i < diningHall.getCount(HouseColor.GREEN); i++) {
      ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.GREEN)));
      student.setFitWidth(20);
      student.setPreserveRatio(true);
      GridPane.setHalignment(student, HPos.CENTER);
      GridPane.setValignment(student, VPos.CENTER);
      studentHallGrid.add(student, 0, 9 - i);
    }

    //updating RED table
    for (int i = 0; i < diningHall.getCount(HouseColor.RED); i++) {
      ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.RED)));
      student.setFitWidth(20);
      student.setPreserveRatio(true);
      GridPane.setHalignment(student, HPos.CENTER);
      GridPane.setValignment(student, VPos.CENTER);
      studentHallGrid.add(student, 1, 9 - i);
    }

    //updating YELLOW table
    for (int i = 0; i < diningHall.getCount(HouseColor.YELLOW); i++) {
      ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.YELLOW)));
      student.setFitWidth(20);
      student.setPreserveRatio(true);
      GridPane.setHalignment(student, HPos.CENTER);
      GridPane.setValignment(student, VPos.CENTER);
      studentHallGrid.add(student, 0, 9 - i);
    }

    //updating PINK table
    for (int i = 0; i < diningHall.getCount(HouseColor.PINK); i++) {
      ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.PINK)));
      student.setFitWidth(20);
      student.setPreserveRatio(true);
      GridPane.setHalignment(student, HPos.CENTER);
      GridPane.setValignment(student, VPos.CENTER);
      studentHallGrid.add(student, 0, 9 - i);
    }

    //updating BLUE table
    for (int i = 0; i < diningHall.getCount(HouseColor.BLUE); i++) {
      ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.BLUE)));
      student.setFitWidth(20);
      student.setPreserveRatio(true);
      GridPane.setHalignment(student, HPos.CENTER);
      GridPane.setValignment(student, VPos.CENTER);
      studentHallGrid.add(student, 0, 9 - i);
    }

    //updating professore table
    profTableGrid.getChildren().clear();
    ProfessorHolder profHold = Controller.getController().getGameState().getPlayingField().getProfessorHolder();
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.GREEN)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.GREEN)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      profTableGrid.add(professor, 0, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.RED)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.RED)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      profTableGrid.add(professor, 1, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.YELLOW)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.YELLOW)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      profTableGrid.add(professor, 2, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.PINK)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.PINK)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      profTableGrid.add(professor, 3, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.BLUE)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.BLUE)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      profTableGrid.add(professor, 4, 0);
    }

    //updating entrance
    entranceGrid.getChildren().clear();
    Students entrance = player.getDashboard().getEntrance();
    int count = 0;
    for (HouseColor color : HouseColor.values()) {
      for (int i = 0; i < entrance.getCount(color); i++) {
        count++;
        ImageView student = new ImageView(new Image(studentColorToPath.get(color)));
        student.setFitWidth(20);
        student.setPreserveRatio(true);
        GridPane.setHalignment(student, HPos.CENTER);
        GridPane.setValignment(student, VPos.CENTER);
        if (count < 5) {
          entranceGrid.add(student, count, 0);
        } else {
          entranceGrid.add(student, 9 - count, 1);
        }

      }
    }
    //updating towers
    dashboardTowers.getChildren().clear();
    for (int i = 0; i < player.getDashboard().getTowers().count; i++) {
      ImageView tower = new ImageView(new Image(towerColorToPath.get(player.getColorTeam())));
      tower.setFitWidth(20);
      tower.setPreserveRatio(true);
      dashboardTowers.getChildren().add(tower);
    }
  }

  private void updatePlayerGrid() {
    otherPlayersGrid.getStyleClass().add("grid-players");
    List<Player> players = Controller.getController().getGameState().getPlayers();
    int i = 0;
    int pIndex = 0;
    for (Player player : players) {
      if (!player.getNickname().equals(Controller.getController().getNickname())) {
        ImageView wizardIcon = new ImageView(new Image("/assets/wizards/wizard-" + pIndex + ".jpg"));
        wizardIcon.setFitWidth(40);
        wizardIcon.setPreserveRatio(true);
        wizardIcon.getStyleClass().add("image-wizard");
        Label nickname = new Label(player.getNickname(), wizardIcon);
        nickname.getStyleClass().add("label-nicknames");
        nickname.setCursor(Cursor.HAND);
        nickname.setContentDisplay(ContentDisplay.RIGHT);
        //TODO: add show showDashboard action on click to image, maybe it depends on the grid?
        otherPlayersGrid.add(nickname, 0, i);
        GridPane.setHalignment(nickname, HPos.RIGHT);

        VBox counters = new VBox();
        counters.setPadding(new Insets(5, 0, 0, 0));
        ImageView cardIcon = new ImageView(new Image("/assets/misc/card-icon.png"));
        cardIcon.setFitWidth(20);
        cardIcon.setPreserveRatio(true);
        Label cardAmount = new Label("x" + player.getCards().size(), cardIcon);
        cardAmount.getStyleClass().add("label-cardamount");
        counters.getChildren().add(cardAmount);

        ImageView towerIcon = new ImageView(new Image("/assets/realm/tower-" + player.getColorTeam() + ".png"));
        towerIcon.setFitWidth(10);
        towerIcon.setPreserveRatio(true);
        Label towerAmount = new Label("x" + player.getDashboard().getTowers().count, towerIcon);
        VBox.setMargin(towerAmount, new Insets(3, 0, 0, 0));
        counters.getChildren().add(towerAmount);
        otherPlayersGrid.add(counters, 1, i);
        i++;
      }
      pIndex++;
    }
  }

  //TODO: redo with tilepane or girdpane
  private void updateAssistCards() {
    assistCards.getChildren().clear();
    ArrayList<AssistantCard> cards = Controller.getController().getGameState().getPlayer(Controller.getController().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.value + ".png");
      img.setImage(new Image(String.valueOf(imgPath)));
      img.setFitWidth(160);
      img.setId(card.toString());
      img.setPreserveRatio(true);
      img.setCursor(Cursor.HAND);
      img.setOnMouseClicked(this::playAssistCardAction);
      //TODO: set highlight or zoom on mouse hover maybe cursor change
      assistCards.getChildren().add(img);
    });
    /*assistCards.getChildren().add(new)*/
  }

  private void createIslandsGrid() {
    List<Island> islands = Controller.getController().getGameState().getPlayingField().getIslands();
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

  private void createCloudGrid() {
    Controller.getController().getGameState().getPlayingField().getClouds().forEach(
            cloud -> {
              cloudBox.getChildren().add(createCloud(cloud));
            });
  }

  private AnchorPane createCloud(Cloud cloud) {
    AnchorPane cloudPane = new AnchorPane();
    cloudPane.setPrefWidth(150);
    cloudPane.setPrefHeight(150);
    cloudPane.setCursor(Cursor.HAND);

    ImageView cloudImg = new ImageView(new Image("/assets/realm/cloud.png"));
    cloudImg.setFitWidth(150);
    cloudImg.setPreserveRatio(true);
    cloudPane.getChildren().add(cloudImg);

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

    Label redStudent = new Label("x" + cloud.getStudents().getCount(HouseColor.RED), red);
    cloudPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 35.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    Label pinkStudent = new Label("x" + cloud.getStudents().getCount(HouseColor.PINK), pink);
    cloudPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 35.0);
    AnchorPane.setLeftAnchor(pinkStudent, 95.0);
    Label greenStudent = new Label("x" + cloud.getStudents().getCount(HouseColor.GREEN), green);
    cloudPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    Label blueStudent = new Label("x" + cloud.getStudents().getCount(HouseColor.BLUE), blue);
    cloudPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);
    AnchorPane.setLeftAnchor(blueStudent, 95.0);
    Label yellowStudent = new Label("x" + cloud.getStudents().getCount(HouseColor.YELLOW), yellow);
    cloudPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 35.0);
    AnchorPane.setLeftAnchor(yellowStudent, 65.0);

    return cloudPane;
  }

  @Override
  public void start() {
    super.start();
    Controller.getController().addListener(this, GAMEDATA_EVENT.tag);

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
    Controller.getController().removeListener(this, GAMEDATA_EVENT.tag);
  }
}
