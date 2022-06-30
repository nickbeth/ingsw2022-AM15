package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;
import it.polimi.ingsw.eriantys.model.entities.Island;

import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;
import static it.polimi.ingsw.eriantys.cli.views.IslandView.drawIsland;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;

public class IslandsView extends View {
  private final List<Island> islands;
  private final int motherNaturePos;
  private final int maxColumns = 2;

  public IslandsView(List<Island> islands, int motherNaturePos) {
    this.islands = islands;
    this.motherNaturePos = motherNaturePos;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(CustomPrintStream o) {
    StringBuilder stringBuilder = new StringBuilder();

    // Populate the matrix
    int rows = drawIsland(islands.get(0), 0, 0).split(System.lineSeparator()).length;
    String[][] matrix = new String[islands.size()][rows];
    for (int i = 0; i < islands.size(); i++) {
      matrix[i] = drawIsland(islands.get(i), (i + 1), motherNaturePos).split(System.lineSeparator());
    }


    int progression = 0;
    while (islands.size() - progression >= maxColumns) {

      // Build the stripes
      for (int islandIndex = progression; islandIndex < islands.size() / 2; islandIndex++) {
//      for (int islandIndex = progression; islandIndex < (progression / 2)  + maxColumns; islandIndex++) {
        for (int row = 0; row < rows; row++) {
          stringBuilder
              .append(matrix[islandIndex][row])
              .append(matrix[matrix.length - islandIndex - 1][row]);
          stringBuilder.append(System.lineSeparator());
        }
        progression += maxColumns;
      }
    }

    // Build last stripes
    if (islands.size() % 2 == 1) {
      for (int row = 0; row < rows; row++) {
        stringBuilder
            // Passing middle island
            .append(centredIsland(matrix[islands.size() / 2][row]))
            .append(System.lineSeparator());
      }
    }

    o.append(System.lineSeparator());

    // Title of the section
    o.println(centredTitle("ISLANDS"), YELLOW);

    // Write the content
    o.append(stringBuilder);

    // Writes a "-" separator
    o.println(centredTitle("-"), YELLOW);
  }

  private String centredIsland(String row) {
    int baseRowLength = "╭────────────────────────────────╮".length();
    int nPadding = (int) (Math.floor((double) baseRowLength / 2));

    return PADDING.repeat(nPadding) + row;
  }

  private String centredTitle(String title) {
    int baseRowLength = "╭────────────────────────────────╮".length();
    int nPadding = baseRowLength - (int) (Math.floor((double) title.length() / 2));

    return "-".repeat(nPadding) + title + "-".repeat(nPadding - 1);
  }
}

