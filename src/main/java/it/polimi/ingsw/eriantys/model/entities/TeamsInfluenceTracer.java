package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

import java.util.*;

// todo Test
public record TeamsInfluenceTracer(EnumMap<TowerColor, Integer> teamsInfluence) {
  public void setInfluence(TowerColor team, int influence) {
    teamsInfluence.put(team, influence);
  }

  public Integer getInfluence(TowerColor team) {
    try {
      return teamsInfluence.get(team);
    } catch (Exception e) {
      Logger.error("INFLUENCE HAS NOT BEEN SET YET");
      return null;
    }
  }

  public Optional<TowerColor> getMostInfluential() {
    // Get the most influential team
    Logger.debug(this);
    try {
      Map.Entry<TowerColor, Integer> maxEntry = Collections.max(teamsInfluence.entrySet(), Map.Entry.comparingByValue());
      // Check if 2 teams have the same influence value and return an empty Optional if so
      for (var t : teamsInfluence.entrySet()) {
        // If there's another team with the same max influence
        if (maxEntry.getValue().equals(t.getValue()) && !maxEntry.getKey().equals(t.getKey())) {
          // Then there's no most influential team
          Logger.debug("Most influential: " + Optional.empty());
          return Optional.empty();
        }
      }
      Logger.debug("Most influential: " + maxEntry.getKey());
      return Optional.of(maxEntry.getKey());
    } catch (NoSuchElementException e) {
      Logger.error("INFLUENCES HAVE NOT BEEN SET YET");
      return Optional.empty();
    }
  }

  public void updateInfluence(Island island, ProfessorHolder professorHolder) {
    // Initialize influences
    for (var color : TowerColor.values()) setInfluence(color, 0);

    // Updates influences
    for (TowerColor team : TowerColor.values()) {
      int influence = 0;

      // Update influence based on color
      for (var color : HouseColor.values()) {
        if (professorHolder.hasProfessor(team, color))
          influence += island.getStudents().getCount(color);
      }
      // Update influence based on tower
      if (island.getTowerColor().isPresent() && island.getTowerColor().get().equals(team))
        influence += island.getTowerCount();

      setInfluence(team, influence);
    }
  }

  @Override
  public String toString() {
    StringBuilder print = new StringBuilder();
    for (var color : TowerColor.values())
      print.append("\nTeam: ").append(color).append(" Influence: ").append(teamsInfluence.get(color));
    return "\nTeamsInfluenceTracer: " + print;
  }
}
