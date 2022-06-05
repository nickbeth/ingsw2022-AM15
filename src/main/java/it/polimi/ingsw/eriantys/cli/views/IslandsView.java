package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.VERTICAL;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;
import static it.polimi.ingsw.eriantys.cli.views.IslandView.drawIsland;
import static java.lang.Enum.valueOf;

public class IslandsView extends View {
  private List<Island> islands;
  private int motherNaturePos;

  public IslandsView(List<Island> islands, int motherNaturePos) {
    this.islands = islands;
    this.motherNaturePos = motherNaturePos;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(PrintStream o) {
    int maxColumns = 2;

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
        for (int row = 0; row < rows; row++) {
          o
              .append(matrix[islandIndex][row])
              .append(matrix[matrix.length - islandIndex - 1][row]);
          o.append(System.lineSeparator());
        }
        progression += maxColumns;
      }
    }

    // Build last stripes
    if (islands.size() % 2 == 1) {
      for (int row = 0; row < rows; row++) {
        o
            .append(centred(matrix[(int) Math.floor((double) islands.size() / 2)][row]))
            .append(System.lineSeparator());
      }
    }
  }

  private String centred(String row) {
    int baseRowLenght = "╭────────────────────────────────╮".length();
    int nPadding = baseRowLenght / 2;

    return PADDING.repeat(nPadding) + row;
  }
}

