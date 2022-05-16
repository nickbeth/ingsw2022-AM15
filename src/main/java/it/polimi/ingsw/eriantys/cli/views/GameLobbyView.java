package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;

import static it.polimi.ingsw.eriantys.cli.utils.Util.PADDING;
import static it.polimi.ingsw.eriantys.cli.utils.Util.printColored;

public class GameLobbyView extends View {
  private GameInfo gameInfo;

  public GameLobbyView(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(PrintStream o) {
    o.append(printColored("GAMEINFO:", HouseColor.RED)).append("\n");
    o.println("Game mode: " + gameInfo.getMode().toString());
    o.println(gameInfo.getMaxPlayerCount() + " player game");
    o.println("\nConnected players:");
    gameInfo.getJoinedPlayers().forEach((key, value) -> {
      o.print(key);
      if (value != null)
        o.append(PADDING).append(printColored(value.toString(), value)).append("\n");
    });
  }
}
