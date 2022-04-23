package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.List;

// todo trasferire la javadoc dalle action alla interfaccia
public interface IGameService {
  void dropStudents(List<Students> diningList, HouseColor color, int amount, StudentBag bag);
  void ignoreColorInfluence(HouseColor ignoredColor, PlayingField playingField);
  void lockIsland(Island island);
  void pickAssistantCard(Player player, int cardIndex);

  /**
   * Removes the students from the given cloud and moves them to the given entrance
   * @param cloud
   * @param dashboard
   */
  void pickCloud(Cloud cloud, Dashboard dashboard);

  /**
   * Executes the movements: <br/>
   * - removes given students from src <br/>
   * - adds given students to dest
   */
  void placeStudents(List<StudentMovement> movements);

  /**
   * Refills Clouds with the given students and removes them from the bag
   */
  void refillClouds(StudentBag studentBag, List<Cloud> clouds, List<Students> cloudStudentsList);
  void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players);
}
