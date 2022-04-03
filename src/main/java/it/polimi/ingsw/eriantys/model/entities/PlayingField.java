package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.eriantys.RuleBook.STUDENT_PER_COLOR_SETUP;

// TODO
public class PlayingField {
  private ArrayList<Island> islands;
  private ArrayList<Cloud> clouds;
  private StudentBag studentBag;
  //    private EnumMap<TowerColor, Integer> influenceTracer;
  private EnumMap<HouseColor, TowerColor> professorHolder;
  private int coins;
  private int motherNaturePosition;
    /* TODO character card features still need implementation
    private ArrayList<CharacterCard> characterCards;
    private CharacterCard playedCaracterCard;
    */

  /**
   * Initializes playing field and all of its components
   *
   * @param ruleBook
   */
  public PlayingField(RuleBook ruleBook) {
//        influenceTracer = new EnumMap<>(TowerColor.class);
    professorHolder = new EnumMap<>(HouseColor.class);
    //initializing islands and student bag
    islands = new ArrayList<>();
    studentBag = new StudentBag();
    studentBag.initStudents(STUDENT_PER_COLOR_SETUP);
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      if (i == 0 || (i == RuleBook.ISLAND_COUNT / 2))
        islands.add(new Island());
      else
        islands.add(new Island(takeStudentFromBag()));
    }
    //definitive initialization of StudentBag
    studentBag.initStudents(RuleBook.STUDENT_PER_COLOR - STUDENT_PER_COLOR_SETUP);
    //initializing Clouds
    clouds = new ArrayList<>();
    for (int i = 0; i < ruleBook.cloudCount; i++) {
      Students tempStudents = new Students();
      for (int j = 0; j < ruleBook.playableStudentCount; j++) {
        HouseColor student = takeStudentFromBag();
        tempStudents.addStudent(student);
      }
      clouds.add(new Cloud(tempStudents));
    }
    // the cloud count is the same as the number of players
    if (ruleBook.gameMode == GameMode.EXPERT) coins = RuleBook.TOTAL_COINS - ruleBook.cloudCount;
    else coins = 0;
    motherNaturePosition = 0;
  }

  //TODO manage lock island in merge Island

  /**
   * merges Islands[islandIndex] with adjacent islands with the same TowerColor
   *
   * @param islandIndex
   */
  public void mergeIslands(int islandIndex) {
    Island nextIsland = islands.get(islandIndex + 1 % RuleBook.ISLAND_COUNT);
    Island prevIsland = (islandIndex == 0) ? islands.get(RuleBook.ISLAND_COUNT - 1) : islands.get(islandIndex - 1);
    Island currIsland = islands.get(islandIndex);
    Logger.debug("prev island:" + islands.indexOf(prevIsland));
    Logger.debug("current island:" + islands.indexOf(currIsland));
    Logger.debug("next island:" + islands.indexOf(nextIsland));
    if (nextIsland.getTowerColor() == currIsland.getTowerColor()) {
      currIsland.addStudents(nextIsland.getStudents());
      currIsland.setTowerCount(currIsland.getTowerCount() + nextIsland.getTowerCount());
      islands.remove(nextIsland);
    }
    if (prevIsland.getTowerColor() == currIsland.getTowerColor()) {
      currIsland.addStudents(prevIsland.getStudents());
      currIsland.setTowerCount(currIsland.getTowerCount() + nextIsland.getTowerCount());
      islands.remove(prevIsland);
      if (islandIndex != 0) motherNaturePosition--;
    }
  }

  /**
   * takes a student from StudentBag
   *
   * @return HouseColor of the student
   */
  public HouseColor takeStudentFromBag() {
    return studentBag.takeRandomStudent();
  }

  public void addStudentToBag(HouseColor color) {
    studentBag.addStudent(color);
  }

  public Cloud getCloud(int cloudIndex) {
    return clouds.get(cloudIndex);
  }

  public Island getIsland(int islandIndex) {
    return islands.get(islandIndex);
  }

  public int getIslandsAmount() {
    return islands.size();
  }

  /**
   * moves motherNature pawn a certain amount
   *
   * @param amount
   */
  public void moveMotherNature(int amount) {
    motherNaturePosition = (motherNaturePosition + amount) % islands.size();
  }

  public int getMotherNaturePosition() {
    return motherNaturePosition;
  }

  /**
   * refills Clouds with students according to rules
   *
   * @param ruleBook
   */
  public void refillClouds(RuleBook ruleBook) {
    for (Cloud c : clouds) {
      Students students = new Students();
      for (int i = 0; i < ruleBook.playableStudentCount; i++) {
        students.addStudent(takeStudentFromBag());
      }
      c.setStudents(students);
    }
  }

  /**
   * Returns true if a certain team has a certain professor
   *
   * @param professor
   * @param team
   * @return boolean
   */
  public boolean hasProfessor(HouseColor professor, TowerColor team) {
    return professorHolder.get(professor) == team;
  }


  public void setProfessorHolder(TowerColor team, HouseColor professor) {
    professorHolder.put(professor, team);
  }

  public void addCoin() {
    coins++;
  }

  public void removeCoin() {
    coins--;
  }

  // getCharacterCards(),getPlayedCharacterCard()

    /* maybe deprecated?
    public void setTower(player: String){

    }*/

  //TODO influence count with CharacterCards

  /**
   * returns the most influential player on the island, if there is none it returns an empty Optional.
   *
   * @param islandIndex
   * @return Optional<TowerColor>
   */
  public Optional<TowerColor> getMostInfluential(int islandIndex) {
    Island island = islands.get(islandIndex);
    Students students = island.getStudents();
    EnumMap<TowerColor, Integer> influenceTracer = new EnumMap<>(TowerColor.class);
    List<HouseColor> possesedProfessors = new ArrayList<>();
    Optional<TowerColor> mostInfluential;

    for (TowerColor team : TowerColor.values()) {
      influenceTracer.put(team, 0);
      possesedProfessors = professorHolder.entrySet().stream().filter(entry -> team.equals(entry.getValue()))
              .map(Map.Entry::getKey).collect(Collectors.toList());
      if (team == island.getTowerColor()) {
        influenceTracer.put(team, influenceTracer.get(team) + island.getTowerCount());
      }
      for (HouseColor professor : possesedProfessors)
        influenceTracer.put(team, influenceTracer.get(team) + students.getValue(professor));
    }
    Logger.debug(influenceTracer.toString());
    //checks if all influences are the same
    if (new HashSet<>(influenceTracer.values()).size() == 1)
      return Optional.empty();
    else
      return mostInfluential = influenceTracer.entrySet().stream()
              .sorted((x, y) -> y.getValue() - x.getValue()).map(Map.Entry::getKey).findFirst();
  }
}


