package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

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
  List<CharacterCard> ccs = game.getPlayingField().getCharacterCards();
  ProfessorHolder professorHolder = game.getPlayingField().getProfessorHolder();
  int motherPosition = game.getPlayingField().getMotherNaturePosition();

  public MenuGame() {
    super();
    eventsToBeListening.add(GAMEDATA_EVENT);
    eventsToBeListening.add(GAME_ENDED);
  }

  final protected void showViewOptions(PrintStream out) {
    out.println("1 - View all");
    out.println("2 - View islands");
    out.println("3 - View dashboards");
    out.println("4 - View clouds");
    if (rules.gameMode.equals(GameMode.EXPERT))
      out.println("5 - CharacterCards");
  }

  final protected void handleViewOptions(String choice) {

    View dashboardsView = new DashboardsView(players, rules, professorHolder);
    View islandsView = new IslandsView(islands, motherPosition);
    View cloudsView = new CloudsView(clouds);
    View ccView = new CharacterCardView(ccs);

    clearConsole();
    switch (choice) {
      // View all field
      case "1" -> {
        ViewGroup viewAll = new ViewGroup()
            .addView(islandsView)
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

      // View all character cards
      case "5" -> {
        if (rules.gameMode.equals(GameMode.EXPERT))
          ccView.draw(out);
      }

      // Simply goes on
      default -> {}
    }
    showOptions();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    // Refresh view and print what's happened
    if (evt.getPropertyName().equals(GAMEDATA_EVENT.tag)) {
      GameAction action = (GameAction) evt.getNewValue();
      clearConsole();
      out.println(action.getDescription());
      if (controller.getGameState().isTurnOf(controller.getNickname()))
        out.println("It's now your turn");
      showOptions();
    }

  }
}
