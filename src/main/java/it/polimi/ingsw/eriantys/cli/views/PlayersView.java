package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;

public class PlayersView extends View {
  private final List<Player> players;
  private final RuleBook rules;

  private int maxLenghtRow = 0;

  public PlayersView(List<Player> players, RuleBook rules) {
    this.players = players;
    this.rules = rules;
  }

  @Override
  public void draw(PrintStream o) {
    String PADDING = PADDING_TRIPLE;
    StringBuilder stringBuilder = new StringBuilder();
    players.forEach(p -> {
      // Print name and color team
      stringBuilder
          .append("Nickname: ").append(p.getNickname())
          .append(PADDING)
          .append("Connection Status: ").append(p.isConnected() ? "online" : "offline")
          .append(PADDING)
          .append("Team: ").append(printColored(p.getColorTeam().toString(), p.getColorTeam()))
          .append(PADDING);
      // Print chosen card if it exists
      p.getChosenCard().ifPresent(card ->
          stringBuilder.append("Chosen card: ")
              .append("value-").append(card.value)
              .append(PADDING)
              .append("moves-").append(p.getMaxMovement())
              .append(PADDING)
      );
      // Print coins if expert mode on
      if (rules.gameMode.equals(GameMode.EXPERT))
        stringBuilder
            .append("Coins: ").append(p.getCoins());

      stringBuilder.append(System.lineSeparator());
    });

    Arrays.stream(stringBuilder.toString().split(System.lineSeparator()))
        .map(String::length)
        .max(Integer::compare)
        .ifPresent(x -> maxLenghtRow = x);

    o.append(System.lineSeparator());

    // Title of the section
    o.append(centredTitle("Players")).append(System.lineSeparator());

    // Write the content
    o.append(stringBuilder);

    // Writes a "-" separator
    o.append(centredTitle("-")).append(System.lineSeparator());
  }

  private String centredTitle(String title) {
    int baseRowLength = maxLenghtRow;
    int nPadding = (baseRowLength / 2) - (int) (Math.floor((double) title.length() / 2));

    return "-".repeat(nPadding) + title + "-".repeat(nPadding - 1);
  }
}
