package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

import java.util.*;

import static it.polimi.ingsw.eriantys.model.RuleBook.STUDENT_PER_COLOR_SETUP;

public class PlayingField {
  class InfluenceModifier {
    private HouseColor ignoredColor;
    private boolean ignoreTower;
    private TowerColor addonTowerColor;
  }

  private ArrayList<Island> islands;
  private ArrayList<Cloud> clouds;
  private StudentBag studentBag;
  private EnumMap<HouseColor, TowerColor> professorHolder;
  private List<TowerColor> teams = new ArrayList<>(); // Active tower colors in this game
  private int coins;
  private int motherNaturePosition;
  private InfluenceModifier influenceModifier = new InfluenceModifier();
  // TODO character card features still need implementation
  // private ArrayList<CharacterCard> characterCards;
  // private CharacterCard playedCaracterCard;

  /**
   * Initializes playing field and all of its components
   *
   * @param ruleBook
   */
  public PlayingField(RuleBook ruleBook) {
    professorHolder = new EnumMap<>(HouseColor.class);
    //initialize islands and student bag
    islands = new ArrayList<>();
    studentBag = new StudentBag();
    studentBag.initStudents(STUDENT_PER_COLOR_SETUP);

    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      if (i == 0 || (i == RuleBook.ISLAND_COUNT / 2))
        islands.add(new Island());
      else
        islands.add(new Island(takeStudentFromBag()));
    }

    // definitive initialization of StudentBag
    studentBag.initStudents(RuleBook.STUDENT_PER_COLOR - STUDENT_PER_COLOR_SETUP);

    // initializing Clouds
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
    if (ruleBook.gameMode == GameMode.EXPERT)
      coins = RuleBook.TOTAL_COINS - ruleBook.cloudCount;
    else
      coins = 0;
    motherNaturePosition = 0;
  }

  //TODO manage lock island in merge Island

  /**
   * merges Islands[islandIndex] with adjacent islands with the same TowerColor
   *
   * @param islandIndex
   */
  public void mergeIslands(int islandIndex) {
    Island nextIsland = islands.get(islandIndex + 1 % islands.size());
    Island prevIsland = (islandIndex == 0) ? islands.get(islands.size() - 1) : islands.get(islandIndex - 1);
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
      currIsland.setTowerCount(currIsland.getTowerCount() + prevIsland.getTowerCount());
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

  public void setIgnoredColor(HouseColor ignoredColor) {
    influenceModifier.ignoredColor = ignoredColor;
  }

  public void setIgnoreTower(boolean ignoreTower) {
    influenceModifier.ignoreTower = ignoreTower;
  }

  public void setAddonTowerColor(TowerColor addonTowerColor) {
    influenceModifier.addonTowerColor = addonTowerColor;
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

  /**
   * Adds a team to the active teams list in this game
   *
   * @param team
   */
  public void addTeam(TowerColor team) {
    teams.add(team);
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
    EnumMap<TowerColor, Integer> teamsInfluence = new EnumMap<>(TowerColor.class);

    for (var team : teams) {
      int influence = 0;

      for (var color : HouseColor.values()) {
        if (hasProfessor(color, team) && color != influenceModifier.ignoredColor) {
          influence += island.getStudents().getCount(color);
        }
      }

      if (team == island.getTowerColor() && !influenceModifier.ignoreTower)
        influence += island.getTowerCount();

      if (team == influenceModifier.addonTowerColor)
        influence += 2;

      teamsInfluence.put(team, influence);
    }

    Logger.debug(teamsInfluence.toString());

    // Get the most influential team
    var maxEntry = Collections.max(teamsInfluence.entrySet(), Map.Entry.comparingByValue());
    // Check if 2 teams have the same influence value and return an empty Optional if so
    for (var team : teams) {
      if (maxEntry.getValue().equals(teamsInfluence.get(team)) && maxEntry.getKey() != team)
        return Optional.empty();
    }

    return Optional.of(maxEntry.getKey());
  }
}


