package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;

import java.io.PrintStream;
import java.util.List;

public class DashboardsView extends View {
  private final List<Player> players;
  private final RuleBook ruleBook;
  private final ProfessorHolder professors;

  public DashboardsView(List<Player> players, RuleBook ruleBook, ProfessorHolder professors) {
    this.players = players;
    this.ruleBook = ruleBook;
    this.professors = professors;
  }

  @Override
  public void draw(PrintStream o) {
    players.forEach(player -> {
      o.printf("%s's dashboard\n", player.getNickname());
      (new DashboardView(ruleBook, player.getDashboard(), professors)).draw(o);
    });
  }
}
