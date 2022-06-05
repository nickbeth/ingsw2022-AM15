package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardCreator;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.model.RuleBook.*;

public class InitiateGameEntities extends GameAction {
  private final List<Students> entrances;
  private final List<Students> islands;
  private final List<Students> clouds;
  private final List<CharacterCardEnum> cardsEnum;

  public InitiateGameEntities(List<Students> entrances, List<Students> islands, List<Students> clouds, List<CharacterCardEnum> cardsEnum) {
    this.entrances = entrances;
    this.islands = islands;
    this.clouds = clouds;
    this.cardsEnum = cardsEnum;
  }

  /**
   * Puts given students in entrances, islands, clouds and from bag.
   * Creates characterCards from given enum
   */
  @Override
  public void apply(GameState gameState) {

    // Initiate players entrances
    List<Player> players = gameState.getPlayers();
    for (int i = 0; i < entrances.size(); i++) {
      players.get(i).getDashboard().getEntrance().setStudents(entrances.get(i));
      gameState.getPlayingField().getStudentBag().removeStudents(entrances.get(i));
    }

    // Initiate islands' students
    for (int i = 0; i < islands.size(); i++) {
      gameState.getPlayingField().getIsland(i).getStudents().setStudents(islands.get(i));
      gameState.getPlayingField().getStudentBag().removeStudents(islands.get(i));
    }

    // Initiate clouds' students
    GameService.refillClouds(
            gameState.getPlayingField().getStudentBag(),
            gameState.getPlayingField().getClouds(),
            clouds);

    // Initiate characterCards
    if(gameState.getRuleBook().gameMode == GameMode.EXPERT) {

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
    RuleBook ruleBook = gameState.getRuleBook();
    boolean isValid = true;

    if(gameState.getRuleBook().gameMode == GameMode.EXPERT)
      isValid = cardsEnum.size() == PLAYABLE_CC_AMOUNT;

    isValid = isValid &&
            entrances.size() == ruleBook.cloudCount &&
            entrances.stream().allMatch((students) -> students.getCount() == ruleBook.entranceSize) &&
            islands.size() == ISLAND_COUNT &&
            islands.stream().allMatch((students) -> (((islands.indexOf(students) == 0 || islands.indexOf(students) == 6 )&& students.getCount() == 0) || students.getCount() == INITIAL_ISLAND_STUDENTS)) &&
            clouds.size() == ruleBook.cloudCount &&
            clouds.stream().allMatch((students) -> students.getCount() == ruleBook.playableStudentCount);
    return isValid;
  }
}
