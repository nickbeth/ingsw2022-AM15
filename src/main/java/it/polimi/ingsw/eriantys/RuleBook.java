package it.polimi.ingsw.eriantys;

import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import org.tinylog.Logger;

public class RuleBook {
  public final GameMode gameMode;
  public final int studentPerColor = 26;
  public final int initialCoins = 1;
  public final int totalCoins = 20;
  public final int islandCount = 12;
  public final int cloudCount;
  public final int playableStudentCount;
  public final int dashboardTowerCount;
  public final int entranceSize;

  private RuleBook(GameMode gameMode, int cloudCount, int playableStudentCount, int dashboardTowerCount, int entranceSize) {
    this.gameMode = gameMode;
    this.cloudCount = cloudCount;
    this.playableStudentCount = playableStudentCount;
    this.dashboardTowerCount = dashboardTowerCount;
    this.entranceSize = entranceSize;
  }

  public static RuleBook makeRules(GameMode gameMode, int playerCount) {
    switch (playerCount) {
      case 2:
      case 4:
        return new RuleBook(gameMode, playerCount, 3, 8, 7);
      case 3:
        return new RuleBook(gameMode, playerCount, 4, 6, 9);
      default:
        Logger.warn("INVALID NUMBER OF PLAYER");
        return null;
    }
  }
}
