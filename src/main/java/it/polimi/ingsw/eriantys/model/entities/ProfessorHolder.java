package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.Arrays;
import java.util.EnumMap;

public record ProfessorHolder(
        EnumMap<HouseColor, TowerColor> professorHolder) {

  /**
   * Returns true if a certain team has a certain professor
   *
   * @param professor
   * @param team
   */
  public boolean hasProfessor(HouseColor professor, TowerColor team) {
    return professorHolder.get(professor) == team;
  }

  public int getHeldProfessorCount(TowerColor team) {
    return (int) Arrays.stream(HouseColor.values()).filter(color -> hasProfessor(color, team)).count();
  }

  public void setProfessorHolder(TowerColor team, HouseColor professor) {
    professorHolder.put(professor, team);
  }
}
