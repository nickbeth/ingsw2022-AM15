package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.EnumMap;

public class CloudsHandler extends SectionHandler {
  private final VBox cloudBox;

  private final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);

  public CloudsHandler(VBox cloudBox) {
    this.cloudBox = cloudBox;
    for (HouseColor color : HouseColor.values())
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");
  }

  @Override
  protected void refresh() {
    super.refresh();
  }

  @Override
  protected void create() {
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

    Label redStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.RED), red);
    cloudPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 35.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    Label pinkStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.PINK), pink);
    cloudPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 35.0);
    AnchorPane.setLeftAnchor(pinkStudent, 95.0);
    Label greenStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.GREEN), green);
    cloudPane.getChildren().add(greenStudent);
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    Label blueStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.BLUE), blue);
    cloudPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);
    AnchorPane.setLeftAnchor(blueStudent, 95.0);
    Label yellowStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.YELLOW), yellow);
    cloudPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 35.0);
    AnchorPane.setLeftAnchor(yellowStudent, 65.0);

    return cloudPane;
  }
}
