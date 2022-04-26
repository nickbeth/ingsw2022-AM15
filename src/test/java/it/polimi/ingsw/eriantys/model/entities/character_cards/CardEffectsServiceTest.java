package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CardEffectsService.getCardEffectsService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardEffectsServiceTest {
  private final ICardEffectsService ccService = getCardEffectsService();

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

    ccService.addToInfluence(2, islands, TowerColor.WHITE);

    islands.forEach(island -> Logger.debug(island.getTeamsInfluenceTracer()));
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
    professorHolder.setProfessorHolder(TowerColor.BLACK, HouseColor.RED);
    islands.get(0).setTowerColor(TowerColor.WHITE);
    islands.get(0).addStudents(temp);
    islands.get(0).setTowerCount(10);
    islands.get(1).setTowerColor(TowerColor.BLACK);
    islands.get(1).setTowerCount(7);
    islands.forEach(island -> island.updateInfluences(professorHolder));

    islands.forEach(island -> Logger.debug(island.getTeamsInfluenceTracer()));
    ccService.ignoreTowers(islands);
    islands.forEach(island -> Logger.debug(island.getTeamsInfluenceTracer()));

    assertEquals(1,islands.get(0).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0,islands.get(0).getTeamsInfluenceTracer().getInfluence(TowerColor.BLACK));
    assertEquals(0,islands.get(1).getTeamsInfluenceTracer().getInfluence(TowerColor.WHITE));
    assertEquals(0,islands.get(1).getTeamsInfluenceTracer().getInfluence(TowerColor.BLACK));
  }

  @Test
  void lockIsland() {
    Island island = new Island();
    ccService.lockIsland(island);
    assertTrue(island.isLocked());
  }

  @Test
  void testAddToInfluence() {
  }

  @Test
  void ignoreColor() {
  }

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
    ccService.dropStudents(entranceList, HouseColor.PINK, 3, studentBag);

    assertEquals(0, entranceList.get(0).getCount(HouseColor.PINK));
    assertEquals(0, entranceList.get(1).getCount(HouseColor.PINK));
    assertEquals(1, entranceList.get(2).getCount(HouseColor.PINK));
  }


}

