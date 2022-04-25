package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers.InfluenceModifierCC;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.tinylog.Logger;

import java.util.*;

public class PlayingField {
  private final List<Island> islands;
  private final List<Cloud> clouds;
  private final StudentBag studentBag;
  private final ProfessorHolder professorHolder;
  private final TeamsInfluenceTracer teamsInfluence;
  private final List<TowerColor> teams = new ArrayList<>(); // Active tower colors in this game
  private int coins;
  private int motherNaturePosition;

  private List<CharacterCard> characterCards;
  private Optional<CharacterCard> playedCharacterCard;

  /**
   * Initializes playing field and all of its components
   */
  public PlayingField(RuleBook ruleBook) {
    professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    teamsInfluence = new TeamsInfluenceTracer(new EnumMap<>(TowerColor.class));

    // Island initialization
    islands = new ArrayList<>();
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      islands.add(new Island());
    }

    // StudentBag initialization
    studentBag = new StudentBag();
    studentBag.initStudents(RuleBook.STUDENT_PER_COLOR);

    // initializing Clouds
    clouds = new ArrayList<>();
    for (int i = 0; i < ruleBook.cloudCount; i++) {
      clouds.add(new Cloud(new Students()));
    }

    // the cloud count is the same as the number of players
    if (ruleBook.gameMode == GameMode.EXPERT)
      coins = RuleBook.TOTAL_COINS - ruleBook.cloudCount;
    else
      coins = 0;
    motherNaturePosition = 0;
  }

  public List<Island> getIslands() {
    return islands;
  }


  public ProfessorHolder getProfessorHolder() {
    return professorHolder;
  }

  //TODO manage lock island in merge Island

  /**
   * Merges islands[islandIndex] with adjacent islands if they have the same TowerColor<br/>
   * If the Merge gets applied also motherNaturePosition gets adjusted
   *
   * @param islandIndex Island index where to calculate mother's nature effect
   */
  public void mergeIslands(int islandIndex) {
    Island nextIsland = islands.get(islandIndex + 1 % islands.size());
    Island prevIsland = (islandIndex == 0) ? islands.get(islands.size() - 1) : islands.get(islandIndex - 1);
    Island currIsland = islands.get(islandIndex);

    Logger.debug("prev island:" + islands.indexOf(prevIsland));
    Logger.debug("current island:" + islands.indexOf(currIsland));
    Logger.debug("next island:" + islands.indexOf(nextIsland));

    //tries merging next island
    if (nextIsland.getTowerColor() == currIsland.getTowerColor()) {
      currIsland.addStudents(nextIsland.getStudents());
      currIsland.setTowerCount(currIsland.getTowerCount() + nextIsland.getTowerCount());
      islands.remove(nextIsland);
      if (islands.indexOf(nextIsland) <= motherNaturePosition) motherNaturePosition--;
    }

    //tries merging prev island
    if (prevIsland.getTowerColor() == currIsland.getTowerColor()) {
      currIsland.addStudents(prevIsland.getStudents());
      currIsland.setTowerCount(currIsland.getTowerCount() + prevIsland.getTowerCount());
      islands.remove(prevIsland);
      if (islandIndex <= motherNaturePosition) motherNaturePosition--;
    }
  }

  public StudentBag getStudentBag() {
    return studentBag;
  }

  public void addStudentToBag(HouseColor color) {
    studentBag.addStudent(color);
  }

  public Cloud getCloud(int cloudIndex) {
    return clouds.get(cloudIndex);
  }

  public List<Cloud> getClouds() {
    return clouds;
  }

  public Island getIsland(int islandIndex) {
    return islands.get(islandIndex);
  }

  public int getIslandsAmount() {
    return islands.size();
  }

  /**
   * Moves motherNature pawn a certain amount of positions
   *
   * @param amount Amount of islands it's wanted to be moved
   */
  public void moveMotherNature(int amount) {
    motherNaturePosition = (motherNaturePosition + amount) % islands.size();
  }

  public int getMotherNaturePosition() {
    return motherNaturePosition;
  }

  /**
   * Returns true if a certain team has a certain professor
   *
   * @param professor
   * @param team
   * @return boolean
   */
  public boolean hasProfessor(HouseColor professor, TowerColor team) {
    return professorHolder.hasProfessor(professor, team);
  }

  public int getHeldProfessorCount(TowerColor team) {
    return professorHolder.getHeldProfessorCount(team);
  }

  public void setProfessorHolder(TowerColor team, HouseColor professor) {
    professorHolder.setProfessorHolder(team, professor);
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

  // CC effect
  // Temporary method, just testing

  public Optional<TowerColor> getMostInfluential(int islandIndex) {
    Island island = islands.get(islandIndex);

    try {
      InfluenceModifierCC card = (InfluenceModifierCC) playedCharacterCard.get();
    } catch (Exception e) {
      Logger.info("Not a modifier");
    }

    for (TowerColor team : teams) {
      int influence = 0;

      for (var color : HouseColor.values()) {
        influence += island.getStudents().getCount(color);
      }
      playedCharacterCard.isPresent();
      teamsInfluence.setInfluence(team, influence);
    }
    Logger.debug(teamsInfluence.toString());

    return teamsInfluence.getMostInflue();
  }

  public void setCharacterCard(int ccIndex) {
    playedCharacterCard = Optional.ofNullable(characterCards.get(ccIndex));
  }


  public Optional<CharacterCard> getPlayedCharacterCard() {
    return playedCharacterCard;
  }
}


