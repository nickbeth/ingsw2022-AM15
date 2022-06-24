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

  private final List<Label> studentlabels = new ArrayList<>();

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
      studentlabels.get(color.ordinal()).setText("×" + cloud.getStudents().getCount(color));
    }
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

    //TODO: write this code in a less verbose way
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

    Label greenStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.GREEN), green);
    cloudPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 40.0);
    AnchorPane.setLeftAnchor(greenStudent, 20.0);
    studentlabels.add(greenStudent);

    Label redStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.RED), red);
    cloudPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 20.0);
    AnchorPane.setLeftAnchor(redStudent, 20.0);
    studentlabels.add(redStudent);

    Label yellowStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.YELLOW), yellow);
    cloudPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 20.0);
    AnchorPane.setLeftAnchor(yellowStudent, 42.0);
    studentlabels.add(yellowStudent);

    Label pinkStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.PINK), pink);
    cloudPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 20.0);
    AnchorPane.setLeftAnchor(pinkStudent, 62.0);
    studentlabels.add(pinkStudent);

    Label blueStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.BLUE), blue);
    cloudPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 40.0);

    AnchorPane.setLeftAnchor(blueStudent, 62.0);
    studentlabels.add(blueStudent);

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