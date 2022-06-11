package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.utils.DataFormats;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class IslandHandler extends SectionHandler {
  private final DebugScreenHandler debugScreenHandler;
  private final AnchorPane islandPane;
  private final Island island;
  private final GameState gameState = Controller.get().getGameState();

  private final EnumMap<TowerColor, String> towerColorToPath = new EnumMap<TowerColor, String>(TowerColor.class);
  private final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);
  private final List<Label> studentlabels = new ArrayList<>();
  private ImageView lockView;
  private ImageView mnView;

  private Label towerLabel;
  private TowerColor towerColor;

  public IslandHandler(AnchorPane islandPane, Island island, DebugScreenHandler debugScreenHandler) {
    this.islandPane = islandPane;
    this.island = island;
    this.debugScreenHandler = debugScreenHandler;
    initMaps();
  }

  @Override
  protected void refresh() {
    PlayingField playingField = gameState.getPlayingField();
    mnView.setVisible(playingField.getMotherNaturePosition() == playingField.getIslands().indexOf(island));
    if (gameState.getGamePhase() == GamePhase.ACTION) {
      if (island.getTowerColor().isPresent()) {
        towerLabel.setVisible(true);
        towerLabel.setText("×" + island.getTowerCount());
      }
      lockView.setVisible(island.isLocked());

      if (gameState.getTurnPhase() == TurnPhase.PLACING) {
        islandPane.setOnDragOver(this::dragOverIsland);
        islandPane.setOnDragDropped(this::dragDropOnIsland);
      }


      TowerColor towerColor;
      if (island.getTowerColor().isPresent()) {
        towerColor = island.getTowerColor().get();
        ImageView tower = new ImageView(new Image(towerColorToPath.get(towerColor)));
        tower.setFitWidth(20);
        tower.setPreserveRatio(true);
        towerLabel = new Label("×" + island.getTowerCount(), tower);
        islandPane.getChildren().add(towerLabel);
        AnchorPane.setBottomAnchor(towerLabel, 75.0);
        AnchorPane.setLeftAnchor(towerLabel, 75.0);
        towerLabel.setVisible(false);
        this.towerColor = towerColor;
      }

      if (this.towerColor != null) {
        towerColor = island.getTowerColor().get();
        towerLabel.setText("×" + island.getTowerCount());
        if (this.towerColor != towerColor) {
          ImageView tower = new ImageView(new Image(towerColorToPath.get(towerColor)));
          tower.setFitWidth(20);
          tower.setPreserveRatio(true);
          towerLabel.setGraphic(tower);
        }
      }

      for (HouseColor color : HouseColor.values()) {
        studentlabels.get(color.ordinal()).setText("×" + island.getStudents().getCount(color));
      }
    }
  }

  @Override
  protected void create() {
    islandPane.setPrefWidth(200);
    islandPane.setPrefHeight(150);

    ImageView islandImg = new ImageView(new Image("/assets/realm/island-" + (gameState.getPlayingField().getIslands().indexOf(island) % 3 + 1) + ".png"));
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

    Label greenStudent = new Label("×" + island.getStudents().getCount(HouseColor.GREEN), green);
    islandPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    studentlabels.add(greenStudent);

    Label redStudent = new Label("×" + island.getStudents().getCount(HouseColor.RED), red);
    islandPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 30.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    studentlabels.add(redStudent);

    Label yellowStudent = new Label("×" + island.getStudents().getCount(HouseColor.YELLOW), yellow);
    islandPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 30.0);
    AnchorPane.setLeftAnchor(yellowStudent, 75.0);
    studentlabels.add(yellowStudent);

    Label pinkStudent = new Label("×" + island.getStudents().getCount(HouseColor.PINK), pink);
    islandPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 30.0);
    AnchorPane.setLeftAnchor(pinkStudent, 115.0);
    studentlabels.add(pinkStudent);

    Label blueStudent = new Label("×" + island.getStudents().getCount(HouseColor.BLUE), blue);
    islandPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);
    AnchorPane.setLeftAnchor(blueStudent, 115.0);
    studentlabels.add(blueStudent);

    lockView = new ImageView(new Image("/assets/realm/lock-icon.png"));
    lockView.setFitWidth(20);
    lockView.setPreserveRatio(true);
    islandPane.getChildren().add(lockView);
    AnchorPane.setBottomAnchor(lockView, 50.0);
    AnchorPane.setLeftAnchor(lockView, 63.0);
    lockView.setVisible(false);

    mnView = new ImageView(new Image("/assets/realm/mothernature.png"));
    mnView.setFitWidth(25);
    mnView.setPreserveRatio(true);
    islandPane.getChildren().add(mnView);
    AnchorPane.setTopAnchor(mnView, 0.0);
    AnchorPane.setLeftAnchor(mnView, 72.5);
    mnView.setVisible(false);
  }

  private void initMaps() {
    //initializing path maps
    for (TowerColor color : TowerColor.values())
      towerColorToPath.put(color, "/assets/realm/tower-" + color + ".png");

    for (HouseColor color : HouseColor.values())
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");
  }

  private void dragOverIsland(DragEvent e) {
    if (e.getDragboard().getContentTypes().contains(DataFormats.HOUSE_COLOR.format))
      e.acceptTransferModes(TransferMode.ANY);
  }

  private void dragDropOnIsland(DragEvent e) {
    e.acceptTransferModes(TransferMode.ANY);
    Dragboard db = e.getDragboard();
    int islandIndex = Controller.get().getGameState().getPlayingField().getIslands().indexOf(island);
    HouseColor color = (HouseColor) db.getContent(DataFormats.HOUSE_COLOR.format);
    Students students = new Students();
    students.addStudent(color);
    if (!Controller.get().sender().sendMoveStudentsToIsland(students, islandIndex))
      debugScreenHandler.showMessage("invalid " + color.toString() + "student drop on island " + islandIndex);
    else
      debugScreenHandler.showMessage(color.toString() + " student was dropped on island " + islandIndex);
  }

}
