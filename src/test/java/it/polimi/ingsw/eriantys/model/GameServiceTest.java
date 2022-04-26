package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentsMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.model.GameService.getGameService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    StudentBag studentBag = new StudentBag();
    gameService.dropStudents(entranceList, HouseColor.PINK, 3, studentBag);

    assertEquals(0, entranceList.get(0).getCount(HouseColor.PINK));
    assertEquals(0, entranceList.get(1).getCount(HouseColor.PINK));
    assertEquals(1, entranceList.get(2).getCount(HouseColor.PINK));
  }

  @Test
  public void ignoreColorInfluence() {
    HouseColor color = HouseColor.PINK;
    PlayingField p = new PlayingField(RuleBook.makeRules(GameMode.EXPERT, 3));
    gameService.ignoreColorInfluence(color, p);
//    assertEquals(color, p.getIgnoredColor());
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
    List<StudentsMovement> moves = new ArrayList<>();
    // SRC Slot has 4 PINKS
    Students entranceSRC = new Students();
//    entranceSRC.addStudentsToSlot(HouseColor.PINK);
//    entranceSRC.addStudentsToSlot(HouseColor.PINK);
//    entranceSRC.addStudentsToSlot(HouseColor.PINK);
//    entranceSRC.addStudentsToSlot(HouseColor.PINK);

    // DEST Slot has none
    Students diningHallDEST = new Students();

    // 3x PINKS from SRC to DEST
//    moves.add(new StudentsMovement(HouseColor.PINK, entranceSRC, diningHallDEST));
//    moves.add(new StudentsMovement(HouseColor.PINK, entranceSRC, diningHallDEST));
//    moves.add(new StudentsMovement(HouseColor.PINK, entranceSRC, diningHallDEST));

//    gameService.placeStudents(moves);

    assertEquals(1, entranceSRC.getCount(HouseColor.PINK));
    assertEquals(3, diningHallDEST.getCount(HouseColor.PINK));

    // SRC is still entranceSRC
    Island island = new Island();
    moves.clear();
//    moves.add(new StudentsMovement(HouseColor.PINK, entranceSRC, island));

//    gameService.placeStudents(moves);

    assertEquals(0, entranceSRC.getCount(HouseColor.PINK));
    assertEquals(1, island.getStudents().getCount(HouseColor.PINK));
  }

  @Test
  void MotherNatureIslandLocked() {
    RuleBook rules = RuleBook.makeRules(GameMode.NORMAL, 2);
    PlayingField field = new PlayingField(rules);
    List<Player> players = new ArrayList<>();
    players.add(new Player(rules, "gino", TowerColor.BLACK, new Students()));
    players.add(new Player(rules, "franco", TowerColor.WHITE, new Students()));

    TowerColor oldIslandColor = field.getIsland(1).getTowerColor().get();
    int oldIslandAmount = field.getIslandsAmount();
    int blackPTowerCount = players.get(0).getDashboard().towerCount();
    int whitePTowerCount = players.get(1).getDashboard().towerCount();
    Logger.debug("\nold amount " + oldIslandAmount);
    field.getIsland(1).setLocked(true);
    gameService.applyMotherNatureEffect(1, field, players);
    Logger.debug("\nnew amount " + field.getIslandsAmount());

    assertEquals(oldIslandAmount, field.getIslandsAmount());
    assertEquals(oldIslandColor, field.getIsland(1).getTowerColor());
    assertEquals(blackPTowerCount, players.get(0).getDashboard().towerCount());
    assertEquals(whitePTowerCount, players.get(1).getDashboard().towerCount());
  }

  @Test
  void MotherNatureNoMostInfluential() {
    RuleBook rules = RuleBook.makeRules(GameMode.NORMAL, 2);
    PlayingField field1 = new PlayingField(rules);
    PlayingField fieldMock = spy(field1);
    doReturn(Optional.empty()).when(fieldMock).getMostInfluential(1);
    List<Player> players = new ArrayList<>();
    players.add(new Player(rules, "gino", TowerColor.BLACK, new Students()));
    players.add(new Player(rules, "franco", TowerColor.WHITE, new Students()));
    Logger.debug("most influential in : " + fieldMock);

    int oldIslandAmount = fieldMock.getIslandsAmount();
    TowerColor oldIslandColor = fieldMock.getIsland(1).getTowerColor().get();
    int blackPTowerCount = players.get(0).getDashboard().towerCount();
    int whitePTowerCount = players.get(1).getDashboard().towerCount();
    Logger.debug("\nold amount " + oldIslandAmount);
    gameService.applyMotherNatureEffect(1, fieldMock, players);
    Logger.debug("\nnew amount " + fieldMock.getIslandsAmount());

    assertEquals(oldIslandAmount, fieldMock.getIslandsAmount());
    assertEquals(oldIslandColor, fieldMock.getIsland(1).getTowerColor());
    assertEquals(blackPTowerCount, players.get(0).getDashboard().towerCount());
    assertEquals(whitePTowerCount, players.get(1).getDashboard().towerCount());
  }

  @Test
  void MotherNatureEffect() {
    RuleBook rules = RuleBook.makeRules(GameMode.NORMAL, 2);
    PlayingField field = new PlayingField(rules);
    PlayingField fieldMock = spy(field);
    doReturn(Optional.of(TowerColor.WHITE)).when(fieldMock).getMostInfluential(1);
    List<Player> players = new ArrayList<>();
    players.add(new Player(rules, "gino", TowerColor.BLACK, new Students()));
    players.add(new Player(rules, "franco", TowerColor.WHITE, new Students()));
    fieldMock.getIsland(1).setTowerColor(TowerColor.BLACK);
    fieldMock.getIsland(1).setTowerCount(1);
    fieldMock.getIsland(2).setTowerColor(TowerColor.WHITE);
    fieldMock.getIsland(2).setTowerCount(1);
    fieldMock.getIsland(0).setTowerColor(TowerColor.WHITE);
    fieldMock.getIsland(0).setTowerCount(1);

    int oldIslandAmount = fieldMock.getIslandsAmount();
    int blackPTowerCount = players.get(0).getDashboard().towerCount();
    int whitePTowerCount = players.get(1).getDashboard().towerCount();
    Logger.debug("\nold amount " + oldIslandAmount);
    gameService.applyMotherNatureEffect(1, fieldMock, players);
    Logger.debug("\nnew amount " + fieldMock.getIslandsAmount());

    assertEquals(oldIslandAmount - 2, fieldMock.getIslandsAmount());
    assertEquals(TowerColor.WHITE, fieldMock.getIsland(0).getTowerColor());
    assertEquals(blackPTowerCount + 1, players.get(0).getDashboard().towerCount());
    assertEquals(whitePTowerCount - 1, players.get(1).getDashboard().towerCount());
  }


}