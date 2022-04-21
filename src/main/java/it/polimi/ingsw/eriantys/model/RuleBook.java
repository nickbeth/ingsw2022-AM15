package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.enums.GameMode;
import org.tinylog.Logger;

public class RuleBook {
  public final GameMode gameMode;
  public final int cloudCount;
  public final int playableStudentCount;
  public final int dashboardTowerCount;
  public final int entranceSize;
  // Same constants in any type of game
  public static final int STUDENT_PER_COLOR = 26;
  public static final int INITIAL_COINS = 1;
  public static final int TOTAL_COINS = 20;
  public static final int ISLAND_COUNT = 12;
  public static final int STUDENT_PER_COLOR_SETUP = 2;
  // Constants based on the type of game
  public static final int PLAYABLE_STUDENTS_2_4 = 3;
  public static final int PLAYABLE_STUDENTS_3 = 3;
  public static final int DASHBOARD_TOWERS_2_4 = 8;
  public static final int DASHBOARD_TOWERS_3 = 6;
  public static final int ENTRANCE_SIZE_2_4 = 7;
  public static final int ENTRANCE_SIZE_3 = 9;
  // Constants for win conditions
  public static final int MIN_ISLAND_COUNT = 3;

  private RuleBook(GameMode gameMode, int cloudCount, int playableStudentCount, int dashboardTowerCount, int entranceSize) {
    this.gameMode = gameMode;
    this.cloudCount = cloudCount;
    this.playableStudentCount = playableStudentCount;
    this.dashboardTowerCount = dashboardTowerCount;
    this.entranceSize = entranceSize;
  }

  /**
   * <p>Factory Method which sets the rules to play with based on the type of game</p>
   * @param gameMode
   * @param playersCount number of players playing the game
   * @return  a RuleBook obj based on gameMode and the number of players
   */
  public static RuleBook makeRules(GameMode gameMode, int playersCount) {
    switch (playersCount) {
      case 2:
      case 4:
        return new RuleBook(gameMode, playersCount,
                PLAYABLE_STUDENTS_2_4, DASHBOARD_TOWERS_2_4, ENTRANCE_SIZE_2_4);
      case 3:
        return new RuleBook(gameMode, playersCount,
                PLAYABLE_STUDENTS_3, DASHBOARD_TOWERS_3, ENTRANCE_SIZE_3);
      default:
        Logger.warn("INVALID NUMBER OF PLAYER");
        return null;
    }
  }
}
