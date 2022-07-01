package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.model.RuleBook.PLAYABLE_CC_AMOUNT;

public class InitiateGameEntities extends GameAction {
  private final List<Students> entrancesStudents;
  private final List<Students> islandsStudents;
  private final List<Students> cloudsStudents;
  private final List<CharacterCardEnum> cardsEnum;

  public InitiateGameEntities(GameInfo gameInfo) {
    RuleBook rules = RuleBook.makeRules(gameInfo.getMode(), gameInfo.getMaxPlayerCount());
    // Initiate character cards
    Random rand = new Random();
    List<CharacterCardEnum> chosenCharacterCards = new ArrayList<>();
    int k = 0;
    while (k < PLAYABLE_CC_AMOUNT) {
      int randomCCIndex = rand.nextInt(0, CharacterCardEnum.values().length);
      CharacterCardEnum randCharacterCards = CharacterCardEnum.values()[randomCCIndex];
      if (!chosenCharacterCards.contains(randCharacterCards)) {
        chosenCharacterCards.add(randCharacterCards);
        k++;
      }
    }

    // Initiate students on island
    StudentBag bag = new StudentBag();
    bag.initStudents(RuleBook.STUDENT_PER_COLOR_SETUP);
    List<Students> studentsOnIslands = new ArrayList<>();
    for (int i = 0; i < RuleBook.ISLAND_COUNT; i++) {
      studentsOnIslands.add(new Students());
      if (i != 0 && i != 6) {
        for (int j = 0; j < RuleBook.INITIAL_ISLAND_STUDENTS; j++)
          studentsOnIslands.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate entrances.
    bag.initStudents(RuleBook.STUDENT_PER_COLOR - RuleBook.STUDENT_PER_COLOR_SETUP);
    List<Students> entrances = new ArrayList<>();
    for (int i = 0; i < gameInfo.getMaxPlayerCount(); i++) {
      entrances.add(new Students());
      for (int j = 0; j < rules.entranceSize; j++) {
        entrances.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    // Initiate clouds.
    List<Students> cloudsStudents = new ArrayList<>();
    for (int i = 0; i < gameInfo.getMaxPlayerCount(); i++) {
      cloudsStudents.add(new Students());
      for (int j = 0; j < rules.playableStudentCount; j++) {
        cloudsStudents.get(i).addStudent(bag.takeRandomStudent());
      }
    }

    this.entrancesStudents = entrances;
    this.islandsStudents = studentsOnIslands;
    this.cloudsStudents = cloudsStudents;
    this.cardsEnum = chosenCharacterCards;
  }

  /**
   * @deprecated {@link #InitiateGameEntities(GameInfo gameInfo)} should be used instead.
   */
  public InitiateGameEntities(List<Students> entrancesStudents, List<Students> islandsStudents, List<Students> cloudsStudents, List<CharacterCardEnum> cardsEnum) {
    this.entrancesStudents = entrancesStudents;
    this.islandsStudents = islandsStudents;
    this.cloudsStudents = cloudsStudents;
    this.cardsEnum = cardsEnum;
  }

  /**
   * Puts given students in entrances, islands, clouds and from bag.
   * Creates characterCards from given enum
   */
  @Override
  public void apply(GameState gameState) {
    StudentBag studentBag = gameState.getPlayingField().getStudentBag();
    List<Island> islands = gameState.getPlayingField().getIslands();
    List<Cloud> clouds = gameState.getPlayingField().getClouds();

    // Initiate players entrances
    List<Player> players = gameState.getPlayers();
    for (int i = 0; i < entrancesStudents.size(); i++) {
      players.get(i).getDashboard().getEntrance().setStudents(entrancesStudents.get(i));
      studentBag.removeStudents(entrancesStudents.get(i));
    }

    // Initiate islands' students
    for (int i = 0; i < islands.size(); i++) {
      islands.get(i).setStudents(islandsStudents.get(i));
      studentBag.removeStudents(islandsStudents.get(i));
    }

    // Initiate clouds' students
    GameService.refillClouds(
        studentBag,
        clouds,
        cloudsStudents);

    // Initiate characterCards
    if (gameState.getRuleBook().gameMode == GameMode.EXPERT) {
      List<CharacterCard> cards = new ArrayList<>();
      cardsEnum.forEach(card -> cards.add(CharacterCardCreator.create(card)));
      modelLogger.info(cards.toString());
      gameState.getPlayingField().setCharacterCards(cards);
    }
  }

  /**
   * Checks:
   * - If the sizes and amount of the given entities is valid
   */
  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
