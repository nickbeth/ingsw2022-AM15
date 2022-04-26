package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import java.util.EnumMap;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.model.enums.TowerColor.BLACK;
import static it.polimi.ingsw.eriantys.model.enums.TowerColor.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class TeamsInfluenceTracerTest {
  private final TeamsInfluenceTracer tracer = new TeamsInfluenceTracer(new EnumMap<>(TowerColor.class));

  @Test
  void setInfluence() {
    tracer.setInfluence(WHITE, 0);
    tracer.setInfluence(BLACK, 10);
    assertEquals(0, tracer.getInfluence(WHITE));
    assertEquals(10, tracer.getInfluence(BLACK));
  }

  @Test
  void getMostInfluential() {
    tracer.setInfluence(WHITE, 0);
    tracer.setInfluence(BLACK, 10);
    tracer.setInfluence(TowerColor.GRAY, 5);
    assertEquals(BLACK, tracer.getMostInfluential().get());
    tracer.setInfluence(TowerColor.GRAY, 10);
    assertEquals(Optional.empty(), tracer.getMostInfluential());
    tracer.setInfluence(WHITE, 0);
    tracer.setInfluence(BLACK, 0);
    tracer.setInfluence(TowerColor.GRAY, 0);
    assertEquals(Optional.empty(), tracer.getMostInfluential());
  }

  @Test
  void updateInfluence() {
    Island island = new Island();
    Students temp = new Students();
    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));

    assertEquals(Optional.empty(), tracer.getMostInfluential());
    Logger.error("Error required for testing");

    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    temp.addStudent(HouseColor.PINK);
    island.addStudents(new Students(temp));

    tracer.updateInfluence(island, professorHolder);
    assertEquals(Optional.empty(), tracer.getMostInfluential());

    professorHolder.setProfessorHolder(WHITE, HouseColor.PINK);
    professorHolder.setProfessorHolder(BLACK, HouseColor.RED);

    tracer.updateInfluence(island, professorHolder);
    assertEquals(WHITE, tracer.getMostInfluential().get());
  }
}