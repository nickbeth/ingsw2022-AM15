package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

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

  /**
   * Return how many professors the given team's holding right now
   *
   * @param team
   */
  public int getHeldProfessorCount(TowerColor team) {
    return (int) Arrays.stream(HouseColor.values()).filter(color -> hasProfessor(team, color)).count();
  }

  /**
   * Returns the team which owns the given professor's color
   *
   * @param professor Color of the professor
   */
  public TowerColor getPossessorOfColor(HouseColor professor) {
    return professorHolder.get(professor);
  }

  /**
   * Set the new owner of the professor
   *
   * @param team      New owner
   * @param professor Professor's color
   */
  public void setProfessorHolder(TowerColor team, HouseColor professor) {
    professorHolder.put(professor, team);
  }

  /**
   * Update professors holders based on who has more students in the dining
   */
  public void updateProfessors(List<Dashboard> dashboards) {
    for (var currentDashboard : dashboards) {
      Arrays.stream(HouseColor.values()).forEach(color -> {
        Optional<Dashboard> opponentDashboard = dashboards
            .stream()
            .filter(d -> d.towerColor().equals(getPossessorOfColor(color)))
            .findFirst();
        int myStudentsCount = currentDashboard.getDiningHall().getCount(color);

        // If no one has that professor yet and i have students -> take it
        if (opponentDashboard.isEmpty() && myStudentsCount != 0) {
          setProfessorHolder(currentDashboard.towerColor(), color);
          return;
        }

        if (opponentDashboard.isPresent()) {
          int opponentStudentsCount = opponentDashboard.get().getDiningHall().getCount(color);
          if (myStudentsCount > opponentStudentsCount) setProfessorHolder(currentDashboard.towerColor(), color);
        }
      });
    }
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
