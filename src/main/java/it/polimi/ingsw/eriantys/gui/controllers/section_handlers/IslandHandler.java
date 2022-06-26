package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.utils.DataFormats;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.util.EnumMap;

import static it.polimi.ingsw.eriantys.gui.controllers.utils.ImagePaths.studentColorToPath;
import static it.polimi.ingsw.eriantys.gui.controllers.utils.ImagePaths.towerColorToPath;

public class IslandHandler extends SectionHandler {
  private final DebugScreenHandler debugScreenHandler;
  private final AnchorPane islandPane;
  private final Island island;
  private final GameState gameState = Controller.get().getGameState();

  private final EnumMap<HouseColor, Label> colorToStudentLabel = new EnumMap<>(HouseColor.class);

  private ImageView lockView;
  private ImageView mnView;

  private Label towerLabel;
  private TowerColor towerColor;

  public IslandHandler(AnchorPane islandPane, Island island, DebugScreenHandler debugScreenHandler) {
    this.islandPane = islandPane;
    this.island = island;
    this.debugScreenHandler = debugScreenHandler;

  }

  @Override
  protected void refresh() {
    PlayingField playingField = gameState.getPlayingField();
    mnView.setVisible(playingField.getMotherNaturePosition() == playingField.getIslands().indexOf(island));

    if (gameState.getGamePhase() == GamePhase.ACTION) {
      // if tower is present on island make it visible
      if (island.getTowerColor().isPresent()) {
        towerLabel.setVisible(true);
        towerLabel.setText("×" + island.getTowerCount());
        // if towerColor changed change graphic
        if (towerColor != island.getTowerColor().get()) {
          towerColor = island.getTowerColor().get();
          ImageView tower = new ImageView(new Image(towerColorToPath.get(towerColor)));
          tower.setFitWidth(20);
          tower.setPreserveRatio(true);
          towerLabel.setGraphic(tower);
        }
      }
      //refresh lock visibility
      lockView.setVisible(island.isLocked());

      //refresh student counters and visibility
      for (HouseColor color : HouseColor.values()) {
        int studentCount = island.getStudents().getCount(color);
        Label studentLabel = colorToStudentLabel.get(color);

        if (studentCount == 0)
          colorToStudentLabel.get(color).setVisible(false);
        else {
          colorToStudentLabel.get(color).setVisible(true);
          studentLabel.setText("×" + island.getStudents().getCount(color));
        }
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

    for (HouseColor color : HouseColor.values()) {
      ImageView studentImg = new ImageView(new Image(studentColorToPath.get(color)));
      studentImg.setFitWidth(20);
      studentImg.setPreserveRatio(true);
      int studentCount = island.getStudents().getCount(color);
      Label studentLabel = new Label("×" + studentCount, studentImg);
      studentLabel.getStyleClass().add("label-counter");
      islandPane.getChildren().add(studentLabel);
      colorToStudentLabel.put(color, studentLabel);
      if (studentCount == 0)
        colorToStudentLabel.get(color).setVisible(false);
    }
    // postions of students on the island
    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.GREEN), 60.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.GREEN), 35.0);

    AnchorPane.setTopAnchor(colorToStudentLabel.get(HouseColor.RED), 30.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.RED), 35.0);

    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.YELLOW), 30.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.YELLOW), 75.0);

    AnchorPane.setTopAnchor(colorToStudentLabel.get(HouseColor.PINK), 30.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.PINK), 115.0);

    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.BLUE), 60.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.BLUE), 115.0);

    mnView = new ImageView(new Image("/assets/realm/mother-nature.png", 25, 100, true, false));
    mnView.setFitWidth(25);
    mnView.setPreserveRatio(true);
    islandPane.getChildren().add(mnView);
    AnchorPane.setTopAnchor(mnView, 0.0);
    AnchorPane.setLeftAnchor(mnView, 72.5);
    mnView.setVisible(false);
    mnView.setOnDragDetected(e -> {
      debugScreenHandler.showMessage("mother nature drag detected");
      Dragboard db = mnView.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      int startIndex = Controller.get().getGameState().getPlayingField().getIslands().indexOf(island);
      content.put(DataFormats.MOTHER_NATURE.format, startIndex);
      db.setContent(content);
      db.setDragView(mnView.getImage());
      e.consume();
    });

    towerLabel = new Label("×" + island.getTowerCount());
    towerLabel.getStyleClass().add("label-counter");
    islandPane.getChildren().add(towerLabel);
    AnchorPane.setBottomAnchor(towerLabel, 75.0);
    AnchorPane.setLeftAnchor(towerLabel, 75.0);
    towerLabel.setVisible(false);

    lockView = new ImageView(new Image("/assets/realm/lock-icon.png"));
    lockView.setFitWidth(50);
    lockView.setPreserveRatio(true);
    islandPane.getChildren().add(lockView);
    AnchorPane.setBottomAnchor(lockView, 50.0);
    AnchorPane.setLeftAnchor(lockView, 60.0);
    lockView.setVisible(false);

    islandPane.setOnDragOver(this::dragOverIsland);
    islandPane.setOnDragDropped(this::dragDropOnIsland);
  }

  private void dragOverIsland(DragEvent e) {
    if (!gameState.getCurrentPlayer().getNickname().equals(Controller.get().getNickname()) ||
            gameState.getGamePhase() != GamePhase.ACTION) {
      // if its not the right gamePhase or player doesn't accept any transfer mode
      e.acceptTransferModes(TransferMode.NONE);
      return;
    }

    if (gameState.getTurnPhase() == TurnPhase.PLACING &&
            e.getDragboard().getContentTypes().contains(DataFormats.HOUSE_COLOR.format)) {
      if (!island.isLocked()) {
        e.acceptTransferModes(TransferMode.ANY);
        return;
      }
    }

    if (gameState.getTurnPhase() == TurnPhase.MOVING &&
            e.getDragboard().getContentTypes().contains(DataFormats.MOTHER_NATURE.format)) {
      e.acceptTransferModes(TransferMode.ANY);
      return;
    }

    if (gameState.getTurnPhase() != TurnPhase.PICKING &&
            e.getDragboard().getContentTypes().contains(DataFormats.CARD_TO_ISLAND.format)) {
      e.acceptTransferModes(TransferMode.MOVE);
      return;
    }

    e.acceptTransferModes(TransferMode.NONE);
  }

  private void dragDropOnIsland(DragEvent e) {
    int islandIndex = Controller.get().getGameState().getPlayingField().getIslands().indexOf(island);
    Dragboard db = e.getDragboard();
    if (db.getContentTypes().contains(DataFormats.HOUSE_COLOR.format)) {
      HouseColor color = (HouseColor) db.getContent(DataFormats.HOUSE_COLOR.format);
      Students students = new Students();
      students.addStudent(color);
      if (!Controller.get().sender().sendMoveStudentsToIsland(students, islandIndex))
        debugScreenHandler.showMessage("invalid " + color.toString() + "student drop on island " + islandIndex);
      else
        debugScreenHandler.showMessage(color.toString() + " student was dropped on island " + islandIndex);
      return;
    }

    if (db.getContentTypes().contains(DataFormats.MOTHER_NATURE.format)) {
      int startIndex = (int) db.getContent(DataFormats.MOTHER_NATURE.format);
      int islandsSize = gameState.getPlayingField().getIslandsAmount();
      int movements = islandIndex - startIndex;
      if (islandIndex < startIndex) {
        movements += islandsSize;
      }
      if (!Controller.get().sender().sendMoveMotherNature(movements))
        debugScreenHandler.showMessage("invalid mother nature drop on island " + islandIndex);
      else
        debugScreenHandler.showMessage("mother nature was dropped on island " + islandIndex);
      return;
    }

    if (db.getContentTypes().contains(DataFormats.CARD_TO_ISLAND.format)) {
      IslandInputCards card = (IslandInputCards) Controller.get().getGameState().getPlayingField().getPlayedCharacterCard();
      card.setIslandIndex(islandIndex);
      if (!Controller.get().sender().sendActivateEffect(card)) {
        e.setDropCompleted(true);
        e.consume();
        debugScreenHandler.showMessage("invalid " + card.getCardEnum() + " effect drop on island " + islandIndex);
      } else {
        debugScreenHandler.showMessage(card.getCardEnum() + " effect was applied on island " + islandIndex);
      }
    }

  }

  public Island getIsland() {
    return island;
  }

  public AnchorPane getPane() {
    return islandPane;
  }
}
