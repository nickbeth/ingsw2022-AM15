package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.VERTICAL;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Optional;

public class IslandView extends View {
  private Island island;

  public IslandView(Island island) {
    this.island = island;
  }

  @Override
  public void draw(PrintStream o) {
    o.append(drawIsland(island, 1, 0));
  }

  public static String drawIsland(Island island, int index, int motherNaturePos) {
    StringBuilder stringBuilder = new StringBuilder();
    Optional<TowerColor> owner = island.getTowerColor();
    int towerCount = island.getTowerCount();

    stringBuilder.append(resetColor());

    // First row
    if (index < 10)
      stringBuilder.append(MessageFormat.format("╭──{0}─────────────────────────────╮", index));
    else
      stringBuilder.append(MessageFormat.format("╭──{0}────────────────────────────╮", index));

    // Put motherNatureLabel if motherNature is present
    if (motherNaturePos == index - 1) {
      String motherNatureLabel = "MOTHER─NATURE";
      int start = 11;

      stringBuilder.replace(start, start + motherNatureLabel.length(), motherNatureLabel);
    }

    if (towerCount != 0 && owner.isPresent()) {
      String towerLabel = TOWER_CHAR + "─x" + towerCount;
      String coloredLabel = colored(towerLabel, owner.get());
      int spacesLeftToTheEnd = 3;
      int end = stringBuilder.length() - spacesLeftToTheEnd;
      int start = end - towerLabel.length();

      stringBuilder.replace(start, start + towerLabel.length(), coloredLabel);
    }
    stringBuilder.append(System.lineSeparator());

    // Second row
    stringBuilder
        .append(VERTICAL.glyph)
        .append(PADDING_DOUBLE);

    for (var color : HouseColor.values()) {
      stringBuilder.append(drawStudents(island.getStudents().getCount(color), color));
    }
    stringBuilder
        .append(VERTICAL.glyph)
        .append(System.lineSeparator());

    // Third row
    stringBuilder.append("╰────────────────────────────────╯");

    // Put lockLabel if lock is present
    if (island.isLocked()) {
      String lockLabel = "LOCKED";
      int spacesLeftToTheEnd = 3;
      int end = stringBuilder.length() - spacesLeftToTheEnd;
      int start = end - lockLabel.length();

      stringBuilder.replace(start, start + lockLabel.length(), lockLabel);
    }
    stringBuilder.append(System.lineSeparator());

    return stringBuilder.toString();
  }

  private static String drawStudents(int amount, HouseColor color) {
    StringBuilder stringBuilder = new StringBuilder();

    if (amount == 0)
      stringBuilder
          .append(PADDING_DOUBLE)
          .append('-')
          .append(PADDING_TRIPLE);
    else if (amount < 10)
      stringBuilder
          .append(colored(STUDENT_CHAR + " x" + amount, color))
          .append(PADDING_DOUBLE);
    else
      stringBuilder
          .append(colored(STUDENT_CHAR + " x" + amount, color))
          .append(PADDING);

    return stringBuilder.toString();
  }

  private static String resetColor() {
    return getColorString(TowerColor.WHITE);
  }
}
