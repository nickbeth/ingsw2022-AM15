package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;

import java.util.*;
import java.util.stream.Collectors;

// TODO
public class PlayingField {
  private ArrayList<Island> islands;
  private ArrayList<Cloud> clouds;
  private EnumMap<HouseColor, Integer> studentBag;
  private EnumMap<TowerColor, Integer> influenceTracer;
  private EnumMap<HouseColor, TowerColor> professorHolder;
  private int coins;
  private int motherNaturePosition;
    /* TODO character card features still need implementation
    private ArrayList<CharacterCard> characterCards;
    private CharacterCard playedCaracterCard;
    */

  public PlayingField(RuleBook ruleBook) {
    influenceTracer = new EnumMap<>(TowerColor.class);
    professorHolder = new EnumMap<>(HouseColor.class);
    //initializing islands and student bag
    islands = new ArrayList<Island>();
    studentBag = new EnumMap<HouseColor, Integer>(HouseColor.class);
    initStudentMap(studentBag, 2);
    for (int i = 0; i < ruleBook.ISLAND_COUNT; i++) {
      if (i < 8 && i != 5) islands.add(new Island(takeStudentFromBag()));
      else islands.add(new Island());
    }
    //definitive initialization of StudentBag
    initStudentMap(studentBag, ruleBook.STUDENT_PER_COLOR - 2);
    //initializing Clouds
    clouds = new ArrayList<Cloud>();
    for (int i = 0; i < ruleBook.cloudCount; i++) {
      EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
      initStudentMap(students, 0);
      for (int j = 0; j < ruleBook.playableStudentCount; j++) {
        HouseColor student = takeStudentFromBag();
        students.put(student, students.get(student) + 1);
      }
      clouds.add(new Cloud(students));
    }
    // the cloud count is the same as the number of players
    if (ruleBook.gameMode == GameMode.EXPERT) coins = ruleBook.TOTAL_COINS - ruleBook.cloudCount;
    else coins = 0;
    motherNaturePosition = 0;
  }

  private void initStudentMap(EnumMap<HouseColor, Integer> map, int value) {
    map.put(HouseColor.RED, value);
    map.put(HouseColor.GREEN, value);
    map.put(HouseColor.BLUE, value);
    map.put(HouseColor.YELLOW, value);
  }

  //TODO merge Islands
  private void mergeIslands(int islandIndex) {
  }

  public HouseColor takeStudentFromBag() {
    Random rand = new Random();
    List<HouseColor> availableColors = studentBag.entrySet().stream()
            .filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    HouseColor color = availableColors.get(rand.nextInt(availableColors.size()));

    int value = studentBag.get(color);
    studentBag.put(color, value - 1);

    return color;
  }

  public Cloud getCloud(int cloudIndex) {
    return clouds.get(cloudIndex);
  }

  public Island getIsland(int islandIndex) {
    return islands.get(islandIndex);
  }

  public void moveMotherNature(int amount) {
    motherNaturePosition = (motherNaturePosition + amount) % islands.size();
  }

  public void refillClouds(RuleBook ruleBook) {
    for (Cloud c : clouds) {
      EnumMap<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
      initStudentMap(students, 0);
      for (int i = 0; i < ruleBook.playableStudentCount; i++) {
        HouseColor student = takeStudentFromBag();
        students.put(student, students.get(student) + 1);
      }
      c.setStudents(students);
    }
  }

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

  // getCharcterCards(),getPlayedCharacterCard()

    /* maybe deprecated?
    public void setTower(player: String){

    }*/

  //TODO influence count
  //public TowerColor getMostInfluential(int islandIndex) {
  //}
}
