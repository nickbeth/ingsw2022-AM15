package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.List;

public interface IGameService {
  // todo trasferire la javadoc dalle action alla interfaccia
  void dropStudents(List<Player> playerList, HouseColor color, int amount);
  void ignoreColorInfluence(HouseColor ignoredColor, PlayingField playingField);
  void lockIsland(Island island);
  void pickAssistantCard(Player player, int cardIndex);
  void pickCloud(Cloud cloud, Player player);
  void placeStudents(List<StudentMovement> entries, Dashboard dashboard, PlayingField playingField);
  void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players);
}
