package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.util.EnumMap;

public class DashboardHandler extends SectionHandler {
  private final GridPane studentHallGrid;
  private final GridPane entranceGrid;
  private final GridPane professorGrid;
  private final TilePane towerTiles;

  private final EnumMap<TowerColor, String> towerColorToPath = new EnumMap<TowerColor, String>(TowerColor.class);
  private final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);
  private final EnumMap<HouseColor, String> professorColorToPath = new EnumMap<>(HouseColor.class);


  public DashboardHandler(GridPane studentHallGrid, GridPane entranceGrid, GridPane professorGrid, TilePane towerTiles) {
    initMaps();
    this.studentHallGrid = studentHallGrid;
    this.entranceGrid = entranceGrid;
    this.professorGrid = professorGrid;
    this.towerTiles = towerTiles;
  }

  @Override
  protected void refresh() {
    GamePhase gamePhase = Controller.getController().getGameState().getGamePhase();
    if (gamePhase == GamePhase.ACTION) {
      create();
    }
  }

  @Override
  protected void create() {
    Player player = Controller.getController().getGameState().getPlayer(Controller.getController().getNickname());
    //populating dining hall
    createDiningHall(player);
    //populating professor table grid
    createProfTable(player);
    //pupulating entrance grid
    createEntrance(player);
    //populating tower tiles
    createTowers(player);
    super.create();
  }

  private void createDiningHall(Player player) {
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
  }

  private void createProfTable(Player player) {
    professorGrid.getChildren().clear();
    ProfessorHolder profHold = Controller.getController().getGameState().getPlayingField().getProfessorHolder();
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.GREEN)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.GREEN)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      professorGrid.add(professor, 0, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.RED)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.RED)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      professorGrid.add(professor, 1, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.YELLOW)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.YELLOW)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      professorGrid.add(professor, 2, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.PINK)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.PINK)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      professorGrid.add(professor, 3, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.BLUE)) {
      ImageView professor = new ImageView(new Image(professorColorToPath.get(HouseColor.BLUE)));
      professor.setFitWidth(20);
      professor.setPreserveRatio(true);
      GridPane.setHalignment(professor, HPos.CENTER);
      GridPane.setValignment(professor, VPos.CENTER);
      professorGrid.add(professor, 4, 0);
    }
  }

  private void createEntrance(Player player) {
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
  }

  private void createTowers(Player player) {
    towerTiles.getChildren().clear();
    for (int i = 0; i < player.getDashboard().getTowers().count; i++) {
      ImageView tower = new ImageView(new Image(towerColorToPath.get(player.getColorTeam())));
      tower.setFitWidth(20);
      tower.setPreserveRatio(true);
      towerTiles.getChildren().add(tower);
    }
  }

  private void initMaps() {
    //initializing path maps
    for (TowerColor color : TowerColor.values())
      towerColorToPath.put(color, "/assets/realm/tower-" + color + ".png");

    for (HouseColor color : HouseColor.values())
      studentColorToPath.put(color, "/assets/realm/student-" + color + ".png");

    for (HouseColor color : HouseColor.values())
      professorColorToPath.put(color, "/assets/realm/professor-" + color + ".png");

  }
}
