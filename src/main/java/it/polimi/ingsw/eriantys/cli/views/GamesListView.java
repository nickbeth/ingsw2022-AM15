package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;
import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;

import java.util.Map;
import java.util.Set;

/**
 * Displays the list of games available to join, received from the server
 */
public class GamesListView extends View {
  protected Map<GameCode, GameInfo> games;

  public GamesListView(Map<GameCode, GameInfo> joinableGameList) {
    this.games = joinableGameList;
  }

  @Override
  public void draw(CustomPrintStream o) {
    StringBuilder builder = new StringBuilder();

    games.forEach((code, info) -> {
      String playersLine = playersContent(info);

      // First row
      builder
          .append("╭─CODE─╦──MODE──╦─")
          .append(playersTitle(info, playersLine.length()))
          .append("╮")
          .append(System.lineSeparator());

      // Content row
      builder
          .append("│ ").append(code).append(" ║")
          .append(" ").append(info.getMode()).append(" ║")
          .append(" ").append(playersContent(info)).append("│")
          .append(System.lineSeparator());

      // Last row
      builder
          .append("╰──────╩────────╩─")
          .append("─".repeat(playersLine.length()))
          .append("╯")
          .append(System.lineSeparator());
    });

    o.print(builder);
  }

  private String playersContent(GameInfo info) {
    Set<String> players = info.getJoinedPlayers();
    StringBuilder content = new StringBuilder();

    players.forEach(nickname -> content.append(nickname).append(" - "));
    // Remove unnecessary separator
    content.replace(content.length() - 3, content.length(), " ");

    int baseTitleLength = playersTitle(info, 0).length();

    return content.length() > baseTitleLength ?
        content.toString() :
        content + " ".repeat(baseTitleLength - content.length());
  }

  private String playersTitle(GameInfo info, int playerContentLength) {
    Set<String> joinedPlayer = info.getJoinedPlayers();
    int leftPlayer = info.getMaxPlayerCount() - joinedPlayer.size();
    String playersTitle = leftPlayer == 0 ?
        "PLAYERS──{full}─" :
        "PLAYERS──{" + leftPlayer + " left}─";

    return playersTitle.length() > playerContentLength ?
        playersTitle :
        playersTitle + "─".repeat(playerContentLength - playersTitle.length());
  }
}
