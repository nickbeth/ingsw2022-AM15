package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.List;

// todo trasferire la javadoc dalle action alla interfaccia
public interface IGameService {
  void dropStudents(List<Students> entranceList, HouseColor color, int amount);
  void ignoreColorInfluence(HouseColor ignoredColor, PlayingField playingField);
  void lockIsland(Island island);
  void pickAssistantCard(Player player, int cardIndex);
  void pickCloud(Cloud cloud, Dashboard dashboard);
  void placeStudents(List<StudentMovement> movements);
  void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players);
}
