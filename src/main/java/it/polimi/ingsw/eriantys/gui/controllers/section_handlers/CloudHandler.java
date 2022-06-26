package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static it.polimi.ingsw.eriantys.gui.controllers.utils.ImagePaths.studentColorToPath;

public class CloudHandler extends SectionHandler {
  private final AnchorPane cloudPane;
  private final DebugScreenHandler debugScreenHandler;

  private final Cloud cloud;
  private final GameState gameState = Controller.get().getGameState();

  private final EnumMap<HouseColor, Label> colorToStudentLabel = new EnumMap<>(HouseColor.class);

  public CloudHandler(AnchorPane cloudPane, Cloud cloud, DebugScreenHandler debugScreenHandler) {
    this.cloudPane = cloudPane;
    this.cloud = cloud;
    this.debugScreenHandler = debugScreenHandler;
  }

  /**
   * Refreshes the students amounts, and cursor
   */
  @Override
  protected void refresh() {
    if (gameState.getTurnPhase() == TurnPhase.PICKING)
      cloudPane.setCursor(Cursor.HAND);
    else
      cloudPane.setCursor(Cursor.DEFAULT);

    for (HouseColor color : HouseColor.values()) {
      int studentCount = cloud.getStudents().getCount(color);
      Label studentLabel = colorToStudentLabel.get(color);

      if (studentCount == 0)
        colorToStudentLabel.get(color).setVisible(false);
      else {
        colorToStudentLabel.get(color).setVisible(true);
        studentLabel.setText("×" + cloud.getStudents().getCount(color));
      }
    }
    //make cloud invisible if there are no students on it
    cloudPane.setVisible(cloud.getStudents().getCount() > 0);
  }

  /**
   * Populates the cloud AnchorPane with images and students amounts
   */
  @Override
  protected void create() {
    cloudPane.setPrefWidth(100);
    cloudPane.setPrefHeight(100);

    ImageView cloudImg = new ImageView(new Image("/assets/realm/cloud.png"));
    cloudImg.setFitWidth(100);
    cloudImg.setPreserveRatio(true);
    cloudPane.getChildren().add(cloudImg);

    for (HouseColor color : HouseColor.values()) {
      ImageView studentImg = new ImageView(new Image(studentColorToPath.get(color)));
      studentImg.setFitWidth(20);
      studentImg.setPreserveRatio(true);
      Label studentLabel = new Label("×" + cloud.getStudents().getCount(color), studentImg);
      studentLabel.getStyleClass().add("label-counter");
      cloudPane.getChildren().add(studentLabel);
      colorToStudentLabel.put(color, studentLabel);
    }

    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.GREEN), 40.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.GREEN), 20.0);

    AnchorPane.setTopAnchor(colorToStudentLabel.get(HouseColor.RED), 20.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.RED), 20.0);

    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.YELLOW), 20.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.YELLOW), 42.0);

    AnchorPane.setTopAnchor(colorToStudentLabel.get(HouseColor.PINK), 20.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.PINK), 62.0);

    AnchorPane.setBottomAnchor(colorToStudentLabel.get(HouseColor.BLUE), 40.0);
    AnchorPane.setLeftAnchor(colorToStudentLabel.get(HouseColor.BLUE), 62.0);

    cloudPane.setOnMouseClicked(e -> pickCloud());
  }

  private void pickCloud() {
    if (gameState.getTurnPhase() == TurnPhase.PICKING) {
      int index = gameState.getPlayingField().getClouds().indexOf(cloud);
      if (!Controller.get().sender().sendPickCloud(index))
        debugScreenHandler.showMessage("cloud " + index + " is invalid");
    }
  }
}
