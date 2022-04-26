package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.List;

public interface ICardEffectsService {
  void ignoreColor(List<Island> islands, HouseColor ignoredColor ,TowerColor teamOwningIgnoredProfessor);
  void ignoreTowers(List<Island> islands);
  void addToInfluence(int amount, List<Island> islands, TowerColor currTeam);
  void dropStudents(List<Students> diningList, HouseColor color, int amount, StudentBag bag);
  void forceMotherNatureEffects(int islandIndex, PlayingField field, List<Player> players);
  void addToMotherNatureMoves(Player currPlayer, int amount);
  void lockIsland(Island island);
  void stealProfessor(Dashboard currDashboard, List<Dashboard> dashboards, ProfessorHolder professorHolder);
}
