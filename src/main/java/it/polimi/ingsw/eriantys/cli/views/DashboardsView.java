package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;

import java.io.PrintStream;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.PADDING;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.PADDING_DOUBLE;
import static it.polimi.ingsw.eriantys.cli.views.IslandView.drawIsland;

public class DashboardsView extends View {
  private final List<Player> players;
  private final RuleBook rules;
  private final ProfessorHolder professors;
  private final int maxColumns = 2;
  private final String PADDING_FROM_EACH_DASHBOARD = PADDING_DOUBLE;

  public DashboardsView(List<Player> players, RuleBook ruleBook, ProfessorHolder professors) {
    this.players = players;
    this.rules = ruleBook;
    this.professors = professors;
  }

  @Override
  public void draw(PrintStream o) {
    DashboardView view = new DashboardView(players.get(0), rules, professors);
    StringBuilder stringBuilder = new StringBuilder();

    // Populate the matrix
    int rows = view.drawDashboard().split(System.lineSeparator()).length;
    String[][] matrix = new String[players.size()][rows];
    for (int i = 0; i < players.size(); i++) {
      view = new DashboardView(players.get(i), rules, professors);
      matrix[i] = view.drawDashboard().split(System.lineSeparator());
    }


    int progression = 0;
    while (players.size() - progression >= maxColumns) {

      // Build the stripes
      for (int row = 0; row < rows; row++) {
        for (int player = progression; player < progression + maxColumns; player++) {
          stringBuilder
              .append(matrix[player][row])
              .append(PADDING_FROM_EACH_DASHBOARD);
        }
        stringBuilder.append(System.lineSeparator());
      }
      progression += maxColumns;
//      stringBuilder.append(System.lineSeparator());
    }

    if (progression < players.size()) {
      // Build last stripes
      for (int row = 0; row < rows; row++) {
        for (int player = progression; player < players.size(); player++) {
          stringBuilder
              .append(centredDashboard(matrix[player][row]))
              .append(PADDING_DOUBLE);
        }
        stringBuilder.append(System.lineSeparator());
      }
    }

    // Title of the section
    o.append(centredTitle("DASHBOARDS")).append(System.lineSeparator());

    // Write the content
    o.append(stringBuilder);

    // Writes a "-" separator
    o.append(centredTitle("-")).append(System.lineSeparator());
  }


  private String centredDashboard(String row) {
    int baseRowLength = "╰───────╩───────────────────────╧─────╩───────╯".length();
    int nPadding = baseRowLength / 2;

    return PADDING.repeat(nPadding) + row;
  }

  private String centredTitle(String row) {
    int baseRowLength = ("╰───────╩───────────────────────╧─────╩───────╯" + PADDING_FROM_EACH_DASHBOARD)
        .repeat(maxColumns).length();
    int nPadding = (baseRowLength / 2) - (int) (Math.floor((double) row.length() / 2));

    return "-".repeat(nPadding) + row + "-".repeat(nPadding);
  }
}
