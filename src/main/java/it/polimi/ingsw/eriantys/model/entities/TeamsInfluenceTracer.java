package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.*;

// todo Test
public record TeamsInfluenceTracer(EnumMap<TowerColor, Integer> teamsInfluence) {
  public void setInfluence(TowerColor team, int influence) {
    teamsInfluence.put(team, influence);
  }

  public Integer getInfluence(TowerColor team) {
    return teamsInfluence.get(team);
  }

  public Optional<TowerColor> getMostInfluential() {
    // Get the most influential team
    Map.Entry<TowerColor, Integer> maxEntry = Collections.max(teamsInfluence.entrySet(), Map.Entry.comparingByValue());
    // Check if 2 teams have the same influence value and return an empty Optional if so
    for (var t : teamsInfluence.entrySet()) {
      // If there's another team with the same max influence
      if (maxEntry.getValue().equals(t.getValue()) && !maxEntry.getKey().equals(t.getKey())) {
        // Then there's no most influential team
        return Optional.empty();
      }
    }
    return Optional.of(maxEntry.getKey());
  }

  public void updateInfluence(Island island, ProfessorHolder professorHolder) {
    for (TowerColor team : TowerColor.values()) {
      int influence = 0;

      // Update influence based on color
      for (var color : HouseColor.values()) {
        if(professorHolder.hasProfessor(team,color))
          influence += island.getStudents().getCount(color);
      }
      // Update influence based on tower
      if(island.getTowerColor().equals(team))
        influence += island.getTowerCount();

      setInfluence(team, influence);
    }
  }
}
