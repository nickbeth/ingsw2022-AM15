package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CloudHandler extends SectionHandler {
  private final AnchorPane cloudPane;
  private Cloud cloud;
  private final List<Label> studentlabels = new ArrayList<>();

  private final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);

  public CloudHandler(AnchorPane cloudPane, Cloud cloud) {
    this.cloudPane = cloudPane;
    this.cloud = cloud;
    for (HouseColor color : HouseColor.values())
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");
  }

  /**
   * Refreshes the students amounts
   */
  @Override
  protected void refresh() {
    for (HouseColor color : HouseColor.values()) {
      studentlabels.get(color.ordinal()).setText("×" + cloud.getStudents().getCount(color));
    }
  }

  /**
   * Populates the cloud AnchorPane with images and students amounts
   */
  @Override
  protected void create() {
    cloudPane.setPrefWidth(150);
    cloudPane.setPrefHeight(150);
    cloudPane.setCursor(Cursor.HAND);

    ImageView cloudImg = new ImageView(new Image("/assets/realm/cloud.png"));
    cloudImg.setFitWidth(150);
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
    AnchorPane.setBottomAnchor(greenStudent, 60.0);
    AnchorPane.setLeftAnchor(greenStudent, 35.0);
    studentlabels.add(greenStudent);

    Label redStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.RED), red);
    cloudPane.getChildren().add(redStudent);
    AnchorPane.setTopAnchor(redStudent, 35.0);
    AnchorPane.setLeftAnchor(redStudent, 35.0);
    studentlabels.add(redStudent);

    Label yellowStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.YELLOW), yellow);
    cloudPane.getChildren().add(yellowStudent);
    AnchorPane.setBottomAnchor(yellowStudent, 35.0);
    AnchorPane.setLeftAnchor(yellowStudent, 65.0);
    studentlabels.add(yellowStudent);

    Label pinkStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.PINK), pink);
    cloudPane.getChildren().add(pinkStudent);
    AnchorPane.setTopAnchor(pinkStudent, 35.0);
    AnchorPane.setLeftAnchor(pinkStudent, 95.0);
    studentlabels.add(pinkStudent);

    Label blueStudent = new Label("×" + cloud.getStudents().getCount(HouseColor.BLUE), blue);
    cloudPane.getChildren().add(blueStudent);
    AnchorPane.setBottomAnchor(blueStudent, 60.0);

    AnchorPane.setLeftAnchor(blueStudent, 95.0);
    studentlabels.add(blueStudent);
  }

}
