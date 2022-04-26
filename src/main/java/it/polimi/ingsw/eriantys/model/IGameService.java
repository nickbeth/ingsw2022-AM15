package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.actions.StudentsMovement;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.List;

// todo trasferire la javadoc dalle action alla interfaccia
public interface IGameService {

  void pickAssistantCard(Player player, int cardIndex);

  /**
   * Removes the students from the given cloud and moves them to the given entrance
   *
   * @param cloud
   * @param dashboard
   */
  void pickCloud(Cloud cloud, Dashboard dashboard);

  /**
   * Executes the movements: <br/>
   * - removes given students from src <br/>
   * - adds given students to dest
   */
//  void placeStudents(List<StudentsMovement> movements);

  void placeStudent(StudentsMovement movements);

  /**
   * Refills Clouds with the given students and removes them from the bag
   */
  void refillClouds(StudentBag studentBag, List<Cloud> clouds, List<Students> cloudStudentsList);

  /**
   * If the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands. <br/>
   * If there isn't a new most influential player nothing changes <br/>
   * Modifies players' tower count if necessary. <br/>
   *
   * @param islandIndex
   * @param field
   * @param players
   */
  void applyMotherNatureEffect(int islandIndex, PlayingField field, List<Player> players);
}
