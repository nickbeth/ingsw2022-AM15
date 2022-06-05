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

  ImageView[] professors = new ImageView[HouseColor.values().length];

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
      refreshProfTable();
      createEntrance();
    }
  }

  @Override
  protected void create() {
    //populating dining hall
    refreshDiningHall();
    //populating professor table grid
    refreshProfTable();
    //pupulating entrance grid
    createEntrance();
    //populating tower tiles
    createTowers();
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

  /**
   * For each table if the amount of students of the model is more than what is shown it adds new images.
   */
  private void refreshDiningHall() {
    Students hall = gameState.getPlayer(nickname).getDashboard().getDiningHall();
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

  /**
   * Creates a student ImageView containing de icon of given color
   */
  private ImageView createStudent(HouseColor color) {
    ImageView student = new ImageView(new Image(studentColorToPath.get(HouseColor.BLUE)));
    student.setFitWidth(20);
    student.setPreserveRatio(true);
    GridPane.setHalignment(student, HPos.CENTER);
    GridPane.setValignment(student, VPos.CENTER);
    return student;
  }

  /**
   * For each professor checks if the player is the holder, if not check if the prof imgview is displayed and removes it
   */
  private void refreshProfTable() {
    TowerColor team = gameState.getPlayer(nickname).getColorTeam();
    ProfessorHolder profHold = gameState.getPlayingField().getProfessorHolder();
    for (HouseColor color : HouseColor.values()) {
      if (profHold.hasProfessor(team, color) && professors[color.ordinal()] == null) {
        ImageView prof = createProfessor(color);
        professors[color.ordinal()] = prof;
        professorGrid.add(prof, color.ordinal(), 0);
      } else {
        professorGrid.getChildren().remove(professors[color.ordinal()]);
        professors[color.ordinal()] = null;
      }
    }
  }

  /**
   * Creates a professor ImageView containing de icon of given color
   */
  private ImageView createProfessor(HouseColor color) {
    ImageView professor = new ImageView(new Image(professorColorToPath.get(color)));
    professor.setFitWidth(20);
    professor.setPreserveRatio(true);
    GridPane.setHalignment(professor, HPos.CENTER);
    GridPane.setValignment(professor, VPos.CENTER);

    return professor;
  }

  private void refereshEntrance() {
    //TODO: method to refresh entrances without recreating
  }


  private void createEntrance() {
    entranceGrid.getChildren().clear();
    Player player = gameState.getPlayer(nickname);
    Students entrance = player.getDashboard().getEntrance();
    int count = 0;
    for (HouseColor color : HouseColor.values()) {
      for (int i = 0; i < entrance.getCount(color); i++) {
        count++;
        ImageView student = createStudent(color);
        if (count < 5) {
          entranceGrid.add(student, count, 0);
        } else {
          entranceGrid.add(student, 9 - count, 1);
        }
      }
    }
  }

  private void createTowers() {
    Player player = gameState.getPlayer(nickname);
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
