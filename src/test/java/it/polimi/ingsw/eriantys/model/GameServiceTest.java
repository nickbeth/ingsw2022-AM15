package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.model.GameService.getGameService;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
  private final IGameService gameService = getGameService();

  @Test
  public void dropStudents() {
    List<Students> entranceList = new ArrayList<>();
    Students temp = new Students();
    // Player1 has no pinks
    entranceList.add(new Students(temp));
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    // Player1 2 pinks
    entranceList.add(new Students(temp));
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    // Player1 4 pinks
    entranceList.add(new Students(temp));

    gameService.dropStudents(entranceList, HouseColor.PINK, 3);

    assertEquals(0, entranceList.get(0).getCount(HouseColor.PINK));
    assertEquals(0, entranceList.get(1).getCount(HouseColor.PINK));
    assertEquals(1, entranceList.get(2).getCount(HouseColor.PINK));
  }

  @Test
  public void ignoreColorInfluence() {
    HouseColor color = HouseColor.PINK;
    PlayingField p = new PlayingField(RuleBook.makeRules(GameMode.EXPERT, 3));
    gameService.ignoreColorInfluence(color, p);
    assertEquals(color, p.getIgnoredColor());
  }

  @Test
  void lockIsland() {
    Island island = new Island();
    gameService.lockIsland(island);
    assertTrue(island.isLocked());
  }

  @Test
  void pickAssistantCard() {
    // no test needed
  }

  @Test
  void pickCloud() {
    Cloud cloud = new Cloud(new Students());
    Dashboard dashboard = new Dashboard(new Students(), 8, TowerColor.WHITE);
    Students temp = new Students();
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    cloud.setStudents(new Students(temp));

    gameService.pickCloud(cloud, dashboard);

    Arrays.stream(HouseColor.values()).forEach((color) ->
            assertEquals(0, cloud.getStudents().getCount(color))
    );
    Arrays.stream(HouseColor.values()).forEach((color) ->
            assertEquals(temp.getCount(color), dashboard.getEntrance().getCount(color))
    );
  }

  @Test
  public void placeStudents() {
    List<StudentMovement> moves = new ArrayList<>();
    // SRC Slot has 4 PINKS
    Students entranceSRC = new Students();
    entranceSRC.addStudentToSlot(HouseColor.PINK);
    entranceSRC.addStudentToSlot(HouseColor.PINK);
    entranceSRC.addStudentToSlot(HouseColor.PINK);
    entranceSRC.addStudentToSlot(HouseColor.PINK);

    // DEST Slot has none
    Students diningHallDEST = new Students();

    // 3x PINKS from SRC to DEST
    moves.add(new StudentMovement(HouseColor.PINK, entranceSRC, diningHallDEST));
    moves.add(new StudentMovement(HouseColor.PINK, entranceSRC, diningHallDEST));
    moves.add(new StudentMovement(HouseColor.PINK, entranceSRC, diningHallDEST));

    gameService.placeStudents(moves);

    assertEquals(1, entranceSRC.getCount(HouseColor.PINK));
    assertEquals(3, diningHallDEST.getCount(HouseColor.PINK));

    // SRC is still entranceSRC
    Island island = new Island();
    moves.clear();
    moves.add(new StudentMovement(HouseColor.PINK, entranceSRC, island));

    gameService.placeStudents(moves);

    assertEquals(0, entranceSRC.getCount(HouseColor.PINK));
    assertEquals(1, island.getStudents().getCount(HouseColor.PINK));
  }

  @Test
  void applyMotherNatureEffect() {
  }
}