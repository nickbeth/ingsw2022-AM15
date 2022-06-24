package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class PlayingField implements Serializable {
  private final List<Island> islands;
  private final List<Cloud> clouds;
  private final StudentBag studentBag;
  private final ProfessorHolder professorHolder;
  private final List<TowerColor> teams = new ArrayList<>(); // Active tower colors in this game
  private Integer motherNaturePosition;
  private int locks;
  private int bank;

  private List<CharacterCard> characterCards = new ArrayList<>();
  private CharacterCard playedCharacterCard;

  /**
   * Initializes playing field and all of its components
   */
  public PlayingField(RuleBook ruleBook) {
    professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));

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
    if (ruleBook.gameMode == GameMode.EXPERT) {
      bank = RuleBook.TOTAL_COINS - ruleBook.cloudCount;
      locks = RuleBook.LOCK_AMOUNT;
    } else {
      bank = 0;
      locks = 0;
    }
    motherNaturePosition = 0;
  }

  public List<Island> getIslands() {
    return islands;
  }

  public ProfessorHolder getProfessorHolder() {
    return professorHolder;
  }

  /**
   * Merges islands[islandIndex] with adjacent islands if they have the same TowerColor<br/>
   * - if the Merge gets applied also motherNaturePosition gets adjusted<br/>
   * - excess locks return to the LockIsland CC<br/>
   */
  public void mergeIslands(int islandIndex) {
    Island nextIsland = islandIndex == islands.size() - 1 ? islands.get(0) : islands.get(islandIndex + 1);
    Island prevIsland = islandIndex == 0 ? islands.get(islands.size() - 1) : islands.get(islandIndex - 1);
    Island currIsland = islands.get(islandIndex);

    currIsland.getTowerColor().ifPresent(currColor -> {
      // Tries merging next island
      nextIsland.getTowerColor().ifPresent(towerColor -> {
        if (towerColor.equals(currColor))
          merge(currIsland, nextIsland);
      });

      // Tries merging prev island
      prevIsland.getTowerColor().ifPresent(towerColor -> {
        if (towerColor.equals(currColor))
          merge(currIsland, prevIsland);
      });
    });
  }

  /**
   * Merges otherIsland into mainIsland
   */
  private void merge(Island mainIsland, Island otherIsland) {
    modelLogger.info("Merging islands: island[{}] <== island[{}] ", islands.indexOf(mainIsland), islands.indexOf(otherIsland));

    // Manage locks, and eventually return them to CC
    if (otherIsland.isLocked() && mainIsland.isLocked())
      locks++;
    else if (otherIsland.isLocked() && !mainIsland.isLocked())
      mainIsland.setLocked(true);
    if (islands.indexOf(otherIsland) <= motherNaturePosition)
      motherNaturePosition--;

    mainIsland.addStudents(otherIsland.getStudents());
    mainIsland.setTowerCount(mainIsland.getTowerCount() + otherIsland.getTowerCount());
    otherIsland.setMerged();
    islands.remove(otherIsland);
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

  public void setLocks(int amount) {
    locks = amount;
  }

  public int getLocks() {
    return locks;
  }

  /**
   * Moves motherNature pawn a certain amount of positions
   *
   * @param amount Amount of islands it's wanted to be moved
   */
  public void moveMotherNature(int amount) {
    motherNaturePosition = (motherNaturePosition + amount) % islands.size();
  }

  public Integer getMotherNaturePosition() {
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
    return professorHolder.hasProfessor(team, professor);
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
    bank++;
  }

  public void removeCoin() {
    bank--;
  }


  // CC effect
  // Temporary method, just testing
  public Optional<TowerColor> getMostInfluential(int islandIndex) {
    Island island = islands.get(islandIndex);
    island.updateInfluences(professorHolder);
    return island.getTeamsInfluenceTracer().getMostInfluential();
  }

  public void setPlayedCharacterCard(int ccIndex) {
    playedCharacterCard = characterCards.get(ccIndex);
  }

  public void setPlayedCharacterCard(CharacterCard cc) {
    playedCharacterCard = cc;

    if (cc != null) {
      for (int i = 0; i < characterCards.size(); i++) {
        if (cc.getCardEnum().equals(characterCards.get(i).getCardEnum()))
          characterCards.set(i, cc);
      }
    }
  }


  public CharacterCard getPlayedCharacterCard() {
    return playedCharacterCard;
  }

  public List<CharacterCard> getCharacterCards() {
    return characterCards;
  }

  public void setCharacterCards(List<CharacterCard> cards) {
    characterCards = cards;
  }

  public int getBank() {
    return bank;
  }

  public void addCoinsToBank(int amount) {
    bank += amount;
  }
}


