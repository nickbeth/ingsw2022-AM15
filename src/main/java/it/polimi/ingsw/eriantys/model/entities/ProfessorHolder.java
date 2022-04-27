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
   * @param team
   * @param professor
   */
  public boolean hasProfessor(TowerColor team, HouseColor professor) {
    return professorHolder.get(professor) == team;
  }

  public int getHeldProfessorCount(TowerColor team) {
    return (int) Arrays.stream(HouseColor.values()).filter(color -> hasProfessor(team, color)).count();
  }

  public TowerColor getProfessorOwner(HouseColor professor) {
    return professorHolder.get(professor);
  }

  public void setProfessorHolder(TowerColor team, HouseColor professor) {
    professorHolder.put(professor, team);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    Arrays.stream(HouseColor.values()).forEach(color -> {
      if (professorHolder.get(color) != null)
        builder.append("\nTeam ").append(professorHolder.get(color))
                .append(" has ").append(color).append(" Professor");
    });
    return "\nProfessor holder: " + builder;
  }
}
