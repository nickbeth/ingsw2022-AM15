package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.utils.IslandPattern;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.EnumMap;
import java.util.List;

public class IslandsHandler extends SectionHandler {
  GridPane islandsGrid;
  private EnumMap<TowerColor, String> towerColorToPath = new EnumMap<TowerColor, String>(TowerColor.class);
  private EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);

  public IslandsHandler(GridPane islandsGrid) {
    initMaps();
    this.islandsGrid = islandsGrid;
  }

  @Override
  protected void refresh() {
    //TODO: write an actual refresh method that doesn't use create() maybe by creating a IslandHandler abstraction.
    GamePhase gamePhase = Controller.get().getGameState().getGamePhase();
    if (gamePhase == GamePhase.ACTION) {
      create();
    }
  }

  @Override
  protected void create() {
    List<Island> islands = Controller.get().getGameState().getPlayingField().getIslands();
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

    Label redStudent = new Label("×" + island.getStudents().getCount(HouseColor.RED), red);
    islandPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 30.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    Label pinkStudent = new Label("×" + island.getStudents().getCount(HouseColor.PINK), pink);
    islandPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 30.0);
    AnchorPane.setLeftAnchor(pinkStudent, 115.0);
    Label greenStudent = new Label("×" + island.getStudents().getCount(HouseColor.GREEN), green);
    islandPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    Label blueStudent = new Label("×" + island.getStudents().getCount(HouseColor.BLUE), blue);
    islandPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);
    AnchorPane.setLeftAnchor(blueStudent, 115.0);
    Label yellowStudent = new Label("×" + island.getStudents().getCount(HouseColor.YELLOW), yellow);
    islandPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 30.0);
    AnchorPane.setLeftAnchor(yellowStudent, 75.0);

    if (island.getTowerColor().isPresent()) {
      ImageView tower = new ImageView(new Image(towerColorToPath.get(island.getTowerColor().get())));
      tower.setFitWidth(20);
      tower.setPreserveRatio(true);
      Label towerCount = new Label("×" + island.getTowerCount(), tower);
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

  private void initMaps() {
    //initializing path maps
    for (TowerColor color : TowerColor.values())
      towerColorToPath.put(color, "/assets/realm/tower-" + color + ".png");

    for (HouseColor color : HouseColor.values())
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");
  }
}
