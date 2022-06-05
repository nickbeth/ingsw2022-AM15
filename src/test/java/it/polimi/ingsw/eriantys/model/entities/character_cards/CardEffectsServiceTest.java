package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.model.enums.TowerColor.BLACK;
import static it.polimi.ingsw.eriantys.model.enums.TowerColor.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CardEffectsServiceTest {

  @Test
  public void addToInfluence() {
    Students temp = new Students();
    temp.addStudent(HouseColor.PINK);

    List<Island> islands = new ArrayList<>();
    islands.add(new Island(new Students()));
    islands.add(new Island(new Students()));
    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.PINK);

    islands.forEach(island -> island.updateInfluences(professorHolder));

    CardService.addToInfluence(2, islands, TowerColor.WHITE);

    islands.forEach(island -> modelLogger.debug(String.valueOf(island.getTeamsInfluenceTracer())));
    islands.forEach(island ->
        assertEquals(2, island.getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE)));
  }

  @Test
  void ignoreTowers() {
    Students temp = new Students();
    temp.addStudent(HouseColor.PINK);

    List<Island> islands = new ArrayList<>();
    islands.add(new Island(new Students()));
    islands.add(new Island(new Students()));
    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.PINK);
    professorHolder.setProfessorHolder(BLACK, HouseColor.RED);
    islands.get(0).setTowerColor(TowerColor.WHITE);
    islands.get(0).addStudents(temp);
    islands.get(0).setTowerCount(10);
    islands.get(1).setTowerColor(BLACK);
    islands.get(1).setTowerCount(7);
    islands.forEach(island -> island.updateInfluences(professorHolder));

    islands.forEach(island -> modelLogger.debug(String.valueOf(island.getTeamsInfluenceTracer())));
    CardService.ignoreTowers(islands);
    islands.forEach(island -> modelLogger.debug(String.valueOf(island.getTeamsInfluenceTracer())));

    assertEquals(1, islands.get(0).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0, islands.get(0).getTeamsInfluenceTracer().getInfluence(BLACK));
    assertEquals(0, islands.get(1).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0, islands.get(1).getTeamsInfluenceTracer().getInfluence(BLACK));
  }

  @Test
  void ignoreColor() {
    Students temp = new Students();
    temp.addStudent(HouseColor.PINK);

    List<Island> islands = new ArrayList<>();
    islands.add(new Island(new Students()));
    islands.add(new Island(new Students()));

    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.PINK);
    professorHolder.setProfessorHolder(BLACK, HouseColor.RED);

    islands.get(0).addStudents(temp);
    temp.addStudent(HouseColor.RED);
    temp.addStudent(HouseColor.RED);
    temp.addStudent(HouseColor.RED);
    islands.get(1).addStudents(temp);
    islands.forEach(island -> island.updateInfluences(professorHolder));

    islands.forEach(island -> modelLogger.debug(String.valueOf(island.getTeamsInfluenceTracer())));
    CardService.ignoreColor(islands, HouseColor.PINK, professorHolder.getProfessorOwner(HouseColor.PINK));
    assertEquals(0, islands.get(0).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0, islands.get(1).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0, islands.get(0).getTeamsInfluenceTracer().getInfluence(BLACK));
    assertEquals(3, islands.get(1).getTeamsInfluenceTracer().getInfluence(BLACK));

    islands.forEach(island -> island.updateInfluences(professorHolder));
    CardService.ignoreColor(islands, HouseColor.RED, professorHolder.getProfessorOwner(HouseColor.RED));
    islands.forEach(island -> modelLogger.debug(String.valueOf(island.getTeamsInfluenceTracer())));
    assertEquals(1, islands.get(0).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(1, islands.get(1).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0, islands.get(0).getTeamsInfluenceTracer().getInfluence(BLACK));
    assertEquals(0, islands.get(1).getTeamsInfluenceTracer().getInfluence(BLACK));
  }

  @Test
  public void dropStudents() {
    List<Students> diningList = new ArrayList<>();
    Students temp = new Students();
    // Player1 has no pinks
    diningList.add(new Students(temp));
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    // Player1 2 pinks
    diningList.add(new Students(temp));
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    // Player1 4 pinks
    diningList.add(new Students(temp));

    StudentBag studentBag = new StudentBag();
    CardService.dropStudents(diningList, HouseColor.PINK, 3, studentBag);

    assertEquals(0, diningList.get(0).getCount(HouseColor.PINK));
    assertEquals(0, diningList.get(1).getCount(HouseColor.PINK));
    assertEquals(1, diningList.get(2).getCount(HouseColor.PINK));
  }

  @Test
  public void forceMotherNatureEffects() {
    RuleBook rules = RuleBook.makeRules(GameMode.NORMAL, 2);
    PlayingField field = new PlayingField(rules);
    PlayingField fieldMock = spy(field);
    doReturn(Optional.of(TowerColor.WHITE)).when(fieldMock).getMostInfluential(1);
    List<Player> players = new ArrayList<>();
    players.add(new Player(rules, "gino", BLACK, new Students()));
    players.add(new Player(rules, "franco", TowerColor.WHITE, new Students()));
    fieldMock.getIsland(1).setTowerColor(BLACK);
    fieldMock.getIsland(1).setTowerCount(1);
    fieldMock.getIsland(2).setTowerColor(TowerColor.WHITE);
    fieldMock.getIsland(2).setTowerCount(1);
    fieldMock.getIsland(0).setTowerColor(TowerColor.WHITE);
    fieldMock.getIsland(0).setTowerCount(1);

    int oldIslandAmount = fieldMock.getIslandsAmount();
    int blackPTowerCount = players.get(0).getDashboard().towerCount();
    int whitePTowerCount = players.get(1).getDashboard().towerCount();
    modelLogger.debug("\nold amount " + oldIslandAmount);
    CardService.forceMotherNatureEffects(1, fieldMock, players);
    modelLogger.debug("\nnew amount " + fieldMock.getIslandsAmount());

    assertEquals(oldIslandAmount - 2, fieldMock.getIslandsAmount());
    assertEquals(TowerColor.WHITE, fieldMock.getIsland(0).getTowerColor().get());
    assertEquals(blackPTowerCount + 1, players.get(0).getDashboard().towerCount());
    assertEquals(whitePTowerCount - 1, players.get(1).getDashboard().towerCount());
  }

  @Test
  public void addToMotherNatureMoves() {
    Player p = mock(Player.class);
    CardService.addToMotherNatureMoves(p, 10);
    assertTrue(true);
  }

  @Test
  void lockIsland() {
    Island island = new Island();
    CardService.lockIsland(island);
    assertTrue(island.isLocked());
  }

  @Test
  public void stealProfessor() {
    // DiningHall set up - 2 player. Both players have 2 pinks
    Students temp = new Students();
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    List<Dashboard> dashes = new ArrayList<>();
    dashes.add(new Dashboard(new Students(), 10, TowerColor.WHITE));
    dashes.add(new Dashboard(new Students(), 10, BLACK));

    dashes.get(0).getDiningHall().setStudents(new Students(temp));
    temp.addStudent(HouseColor.RED);
    dashes.get(1).getDiningHall().setStudents(new Students(temp));

    // Professor holder setup. BLACK has PINK, WHITE tries to STEAL
    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    professorHolder.setProfessorHolder(BLACK, HouseColor.PINK);
    professorHolder.setProfessorHolder(BLACK, HouseColor.RED);

    modelLogger.debug(professorHolder.toString());
    modelLogger.debug("White tries to steal");
    CardService.stealProfessor(dashes.get(0), dashes, professorHolder);
    modelLogger.debug(professorHolder.toString());

    assertEquals(BLACK, professorHolder.getProfessorOwner(HouseColor.RED));
    assertEquals(WHITE, professorHolder.getProfessorOwner(HouseColor.PINK));
  }
}

