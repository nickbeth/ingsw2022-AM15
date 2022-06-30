package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.PADDING_TRIPLE;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;

public class PlayersView extends View {
  private final List<Player> players;
  private final RuleBook rules;

  private int maxLengthRow = 0;

  public PlayersView(List<Player> players, RuleBook rules) {
    this.players = players;
    this.rules = rules;
  }

  @Override
  public void draw(CustomPrintStream o) {
    String PADDING = PADDING_TRIPLE;
    StringBuilder stringBuilder = new StringBuilder();

    players.forEach(p -> {
      // Print name and color team
      stringBuilder
          .append("Nickname: ").append(p.getNickname())
          .append(PADDING)
          .append("Connection Status: ")
          .append(p.isConnected() ?
              colored("online", HouseColor.GREEN)
              : colored("offline", HouseColor.RED))
          .append(PADDING)
          .append("Team: ").append(p.getColorTeam().toString())
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
        .ifPresent(x -> maxLengthRow = x);

    o.append(System.lineSeparator());

    // Title of the section
    o.println(centredTitle("Players"), HouseColor.YELLOW);

    // Write the content
    o.append(stringBuilder);

    // Writes a "-" separator
    o.println(centredTitle("-"), HouseColor.YELLOW);
  }

  private String centredTitle(String title) {
    int baseRowLength = maxLengthRow;
    int nPadding = (baseRowLength / 2) - (int) (Math.floor((double) title.length() / 2));

    return "-".repeat(nPadding) + title + "-".repeat(nPadding - 1);
  }
}
