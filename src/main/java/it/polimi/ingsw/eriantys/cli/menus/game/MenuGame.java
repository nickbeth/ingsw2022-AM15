package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.controller.EventType;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.io.PrintStream;
import java.util.List;

public abstract class MenuGame extends Menu {
  // Linking the game state to menus
  GameState game = controller.getGameState();
  RuleBook rules = game.getRuleBook();
  List<Island> islands = game.getPlayingField().getIslands();
  List<Dashboard> dashboards = game.getDashboards();
  List<CharacterCard> ccs = game.getPlayingField().getCharacterCards();
  ProfessorHolder professorHolder = game.getPlayingField().getProfessorHolder();
  int motherPosition = game.getPlayingField().getMotherNaturePosition();

  public MenuGame() {
    eventsToBeListening.add(EventType.GAMEDATA_EVENT);
  }

  final protected void showViewOptions(PrintStream out) {
    out.println("1 - View all");
    out.println("2 - View islands");
    out.println("3 - View dashboards");
    if (rules.gameMode.equals(GameMode.EXPERT))
      out.println("4 - CharacterCards");
  }

  final protected void handleViewOptions(String choice, PrintStream out) {

    ViewGroup dashboardsView = new ViewGroup();
    dashboards.forEach(d ->
        dashboardsView.addView((new DashboardView(rules, d, professorHolder))));
    View islandsView = new IslandsView(islands, motherPosition);

    switch (choice) {
      // View all field
      case "1" -> (new ViewGroup())
          .addView(islandsView)
          .addView(dashboardsView)
          .draw(out);

      // View all islands
      case "2" -> islandsView.draw(out);

      // View all dashboards
      case "3" -> dashboardsView.draw(out);

      // View all character cards
      case "4" -> {
        if (controller.getGameState().getRuleBook().gameMode.equals(GameMode.EXPERT))
          (new CharacterCardView(ccs)).draw(out);
      }

      // Simply goes on
      default -> {}
    }
  }
}
