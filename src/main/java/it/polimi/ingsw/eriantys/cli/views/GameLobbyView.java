package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.controller.GameLobby;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import static it.polimi.ingsw.eriantys.cli.utils.Util.*;

import java.io.PrintStream;

public class GameLobbyView extends View {
  private GameLobby gameLobby;

  public GameLobbyView(GameLobby gameLobby) {
    this.gameLobby = gameLobby;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(PrintStream o) {
    o.append(printColored("GAMEINFO:", HouseColor.RED)).append("\n");
    o.println("Game mode: " + gameLobby.getMode().toString());
    o.println(gameLobby.getPlayerAmount() + " player game");
    o.println("\nConnected players:");
    gameLobby.getNicknameToTeamColor().forEach((key, value) -> {
      o.print(key);
      if (value != null) o.append(PADDING).append(printColored(value.toString(), value)).append("\n");
    });

  }
}
