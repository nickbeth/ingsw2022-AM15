package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.List;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.GAME_ENDED;

public abstract class MenuGame extends Menu {
  // Linking the game state to menus
  GameState game = controller.getGameState();
  RuleBook rules = game.getRuleBook();
  List<Island> islands = game.getPlayingField().getIslands();
  List<Cloud> clouds = game.getPlayingField().getClouds();
  List<Player> players = game.getPlayers();
  Player currentPlayer = game.getCurrentPlayer();
  Player me = game.getPlayer(controller.getNickname());
  List<CharacterCard> ccs = game.getPlayingField().getCharacterCards();
  ProfessorHolder professorHolder = game.getPlayingField().getProfessorHolder();
  Integer motherPosition = game.getPlayingField().getMotherNaturePosition();

  public MenuGame() {
    super();
    eventsToBeListening.add(GAMEDATA_EVENT);
    eventsToBeListening.add(GAME_ENDED);
    clearConsole();
  }

  final protected void showViewOptions(PrintStream out) {
    out.println();
    out.printf("- GamePhase: %s TurnPhase: %s ------------------------------------------------\n", game.getGamePhase().toString(), game.getTurnPhase().toString());

    if (isMyTurn()) {
      out.println("It's now your turn " + currentPlayer + "(i am actually " + me + ")");
    } else {
      out.println("It's now turn of: " + currentPlayer);
      out.println("Event if it's not your turn, you can see the game.");
    }
    out.println("1 - View all");
    out.println("2 - View islands");
    out.println("3 - View dashboards");
    out.println("4 - View clouds");
    out.println("5 - View my assistant cards");
    out.println("6 - View my dashboard");
    out.println("7 - Show turn orders");
    if (rules.gameMode.equals(GameMode.EXPERT))
      out.println("10 - CharacterCards");
  }

  final protected void handleViewOptions(String choice) {
    View dashboardsView = new DashboardsView(players, rules, professorHolder);
    View islandsView = new IslandsView(islands, motherPosition);
    View cloudsView = new CloudsView(clouds);
    View ccView = new CharacterCardsView(ccs);
    View playersView = new PlayersView(players, rules);

    clearConsole();
    switch (choice) {
      // View all field
      case "1" -> {
        ViewGroup viewAll = new ViewGroup()
            .addView(islandsView)
            .addView(playersView)
            .addView(dashboardsView)
            .addView(cloudsView);
        if (rules.gameMode.equals(GameMode.EXPERT))
          viewAll.addView(ccView);
        viewAll.draw(out);
      }

      // View all islands
      case "2" -> islandsView.draw(out);

      // View all dashboards
      case "3" -> dashboardsView.draw(out);

      // View all character cards
      case "4" -> cloudsView.draw(out);

      // View my assistant cards
      case "5" -> new AssistantCardsView(me).draw(out);

      // View my dashboard
      case "6" -> new DashboardView(me, rules, professorHolder).draw(out);

      // Show turn orders
      case "7" -> {
        out.println("Plan order:");
        game.getPlanningPhaseOrder()
            .forEach(player -> out.print(player + " -> "));

        out.println();

        if (game.getGamePhase().equals(GamePhase.ACTION)) {
          out.println("Action order:");
          game.getActionPhaseOrder()
              .forEach(player -> out.print(player + " -> "));
        }
      }

      case "8" -> playersView.draw(out);

      // View all character cards
      case "10" -> {
        if (rules.gameMode.equals(GameMode.EXPERT))
          ccView.draw(out);
      }

      case "quitgame" -> controller.sender().sendQuitGame();

      // Simply goes on
      default -> {
      }
    }
    showOptions();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    // Refresh view and print what's happened
    if (evt.getPropertyName().equals(GAMEDATA_EVENT.tag)) {
      String actionDescription = (String) evt.getNewValue();
      clearConsole();
      out.println(actionDescription);
      showOptions();
    }
  }

  /**
   * Checks if i amd the current player
   *
   * @return True if so, false otherwise.
   */
  protected boolean isMyTurn() {
    return game.isTurnOf(controller.getNickname());
  }
}
