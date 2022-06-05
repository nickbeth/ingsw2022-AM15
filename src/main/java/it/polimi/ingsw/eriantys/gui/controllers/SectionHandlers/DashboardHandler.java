package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class DashboardHandler extends SectionHandler {
  private final String nickname;
  private final GridPane studentHallGrid;
  private final GridPane entranceGrid;
  private final GridPane professorGrid;
  private final TilePane towerTiles;
  GameState gameState = Controller.get().getGameState();

  private final EnumMap<TowerColor, String> towerColorToPath = new EnumMap<TowerColor, String>(TowerColor.class);
  private final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class);
  private final EnumMap<HouseColor, String> professorColorToPath = new EnumMap<>(HouseColor.class);

  List<ImageView> blueStudents = new ArrayList<>();
  List<ImageView> redStudents = new ArrayList<>();
  List<ImageView> yellowStudents = new ArrayList<>();
  List<ImageView> greenStudents = new ArrayList<>();
  List<ImageView> pinkStudents = new ArrayList<>();


  public DashboardHandler(String nickname, GridPane studentHallGrid, GridPane entranceGrid, GridPane professorGrid, TilePane towerTiles) {
    initMaps();
    this.nickname = nickname;
    this.studentHallGrid = studentHallGrid;
    this.entranceGrid = entranceGrid;
    this.professorGrid = professorGrid;
    this.towerTiles = towerTiles;
  }

  @Override
  protected void refresh() {
    GamePhase gamePhase = gameState.getGamePhase();
    Students entrance = gameState.getPlayer(nickname).getDashboard().getEntrance();
    ProfessorHolder professors = gameState.getPlayingField().getProfessorHolder();
    if (gamePhase == GamePhase.ACTION) {
      refreshDiningHall();
      refreshTowers();

    }
  }

  @Override
  protected void create() {
    Player player = gameState.getPlayer(Controller.get().getNickname());
    //populating dining hall
    refreshDiningHall();
    //populating professor table grid
    createProfTable(player);
    //pupulating entrance grid
    createEntrance(player);
    //populating tower tiles
    createTowers(player);
    super.create();
  }

  private void refreshTowers() {
    Player player = gameState.getPlayer(nickname);
    int tileCount = towerTiles.getChildren().size();
    int towerCount = gameState.getPlayer(nickname).getDashboard().getTowers().count;
    if (towerCount > tileCount) {
      for (int i = 0; i < towerCount - tileCount; i++) {
        ImageView tower = new ImageView(new Image(towerColorToPath.get(player.getColorTeam())));
        tower.setFitWidth(20);
        tower.setPreserveRatio(true);
        towerTiles.getChildren().add(tower);
      }
    } else
      towerTiles.getChildren().remove(0);
  }

  private void refreshDiningHall() {
    Students hall = Controller.get().getGameState().getPlayer(nickname).getDashboard().getDiningHall();
    for (int i = 0; i < hall.getCount(HouseColor.GREEN) - greenStudents.size(); i++) {
      ImageView student = createStudent(HouseColor.GREEN);
      greenStudents.add(student);
      studentHallGrid.add(student, 0, 9 - greenStudents.indexOf(student));
    }

    for (int i = 0; i < hall.getCount(HouseColor.RED) - redStudents.size(); i++) {
      ImageView student = createStudent(HouseColor.RED);
      greenStudents.add(student);
      studentHallGrid.add(student, 0, 9 - redStudents.indexOf(student));
    }

    for (int i = 0; i < hall.getCount(HouseColor.YELLOW) - yellowStudents.size(); i++) {
      ImageView student = createStudent(HouseColor.YELLOW);
      yellowStudents.add(student);
      studentHallGrid.add(student, 0, 9 - yellowStudents.indexOf(student));
    }

    for (int i = 0; i < hall.getCount(HouseColor.PINK) - pinkStudents.size(); i++) {
      ImageView student = createStudent(HouseColor.PINK);
      pinkStudents.add(student);
      studentHallGrid.add(student, 0, 9 - pinkStudents.indexOf(student));
    }

    for (int i = 0; i < hall.getCount(HouseColor.BLUE) - blueStudents.size(); i++) {
      ImageView student = createStudent(HouseColor.BLUE);
      blueStudents.add(student);
      studentHallGrid.add(student, 0, 9 - blueStudents.indexOf(student));
    }
  }

  private void refreshProfTable() {
    Player player = gameState.getPlayer(nickname);
    //professorGrid.add();
  }


  private ImageView createStudent(HouseColor color) {
    ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.BLUE)));
    student.setFitWidth(20);
    student.setPreserveRatio(true);
    GridPane.setHalignment(student, HPos.CENTER);
    GridPane.setValignment(student, VPos.CENTER);
    return student;
  }


  private void createProfTable(Player player) {
    professorGrid.getChildren().clear();
    ProfessorHolder profHold = Controller.get().getGameState().getPlayingField().getProfessorHolder();
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.GREEN)) {
      professorGrid.add(createProfessor(HouseColor.GREEN), 0, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.RED)) {
      professorGrid.add(createProfessor(HouseColor.RED), 1, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.YELLOW)) {
      professorGrid.add(createProfessor(HouseColor.YELLOW), 2, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.PINK)) {
      professorGrid.add(createProfessor(HouseColor.PINK), 3, 0);
    }
    if (profHold.hasProfessor(player.getColorTeam(), HouseColor.BLUE)) {
      professorGrid.add(createProfessor(HouseColor.BLUE), 4, 0);
    }
  }

  private ImageView createProfessor(HouseColor color) {
    ImageView professor = new ImageView(new Image(professorColorToPath.get(color)));
    professor.setFitWidth(20);
    professor.setPreserveRatio(true);
    GridPane.setHalignment(professor, HPos.CENTER);
    GridPane.setValignment(professor, VPos.CENTER);

    return professor;
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
