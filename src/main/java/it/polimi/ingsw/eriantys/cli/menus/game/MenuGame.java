package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.InputHandler;
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
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;
import static java.text.MessageFormat.format;

public abstract class MenuGame extends Menu {
  String baseSeparator = "-------------------------------------------------------------------------------------------------------";

  protected View dashboardsView() {
    return new DashboardsView(players(), rules(), professorHolder());
  }

  protected View islandsView() {
    return new IslandsView(islands(), motherPosition());
  }

  protected View cloudsView() {
    return new CloudsView(clouds());
  }

  protected View characterCardsView() {
    return new CharacterCardsView(ccs());
  }

  protected View playersView() {
    return new PlayersView(players(), rules());
  }

  // Linking the game state to menus
  protected GameState game() {
    return controller.getGameState();
  }

  protected GamePhase gamePhase() {
    return game().getGamePhase();
  }

  protected TurnPhase turnPhase() {
    return game().getTurnPhase();
  }

  protected RuleBook rules() {
    return game().getRuleBook();
  }

  protected List<Island> islands() {
    return game().getPlayingField().getIslands();
  }

  protected List<Cloud> clouds() {
    return game().getPlayingField().getClouds();
  }

  protected List<Player> players() {
    return game().getPlayers();
  }

  protected Player currentPlayer() {
    return game().getCurrentPlayer();
  }

  protected Player me() {
    return game().getPlayer(controller.getNickname());
  }

  protected List<CharacterCard> ccs() {
    return game().getPlayingField().getCharacterCards();
  }

  protected ProfessorHolder professorHolder() {
    return game().getPlayingField().getProfessorHolder();
  }

  protected Integer motherPosition() {
    return game().getPlayingField().getMotherNaturePosition();
  }

  public MenuGame() {
    super();
    eventsToBeListening.add(GAMEDATA_EVENT);
    eventsToBeListening.add(PLAYER_CONNECTION_CHANGED);
    eventsToBeListening.add(END_GAME);
    eventsToBeListening.add(DELIBERATE_DISCONNECTION);
  }

  final protected void showViewOptions() {
    out.println();
    StringBuilder openingRow = new StringBuilder(baseSeparator);
    String label = format(
        "GameCode: {0} - Player: {1} - GamePhase: {2} TurnPhase: {3} ",
        controller.getGameCode(),
        me().getNickname(),
        game().getGamePhase().toString(),
        game().getTurnPhase().toString()
    );
    openingRow.replace(1, 1 + label.length(), label);

    // Opening row
    out.println(openingRow.toString(), YELLOW);

    // Turn label
    String turnLabel = isMyTurn() ? "It's now your turn " : "It's now turn of: ";
    out.println(turnLabel + currentPlayer());

    // Option
    StringBuilder options = new StringBuilder();
    out.println("0000 - Quit the game and disconnect", RED);
    options
        .append("1 - View all").append(System.lineSeparator())
        .append("2 - View islands").append(System.lineSeparator())
        .append("3 - View dashboards").append(System.lineSeparator())
        .append("4 - View clouds").append(System.lineSeparator())
        .append("5 - View my assistant cards").append(System.lineSeparator())
        .append("6 - View my dashboard").append(System.lineSeparator())
        .append("7 - Show turn orders").append(System.lineSeparator())
        .append("8 - Show players");
    if (rules().gameMode.equals(GameMode.EXPERT))
      options.append("10 - CharacterCards");
    out.println(options);

    // Optional closing row
    if (!isMyTurn())
      out.println(baseSeparator, YELLOW);
  }

  /**
   * Handle common view options of the GameState
   */
  final protected void handleViewOptions(String choice) {
    boolean refreshMenu = true;

    switch (choice) {
      // View all field
      case "1" -> {
        ViewGroup viewAll = new ViewGroup()
            .addView(islandsView())
            .addView(playersView())
            .addView(dashboardsView())
            .addView(cloudsView());
        if (rules().gameMode.equals(GameMode.EXPERT))
          viewAll.addView(characterCardsView());
        viewAll.draw(out);
      }

      // View all islands
      case "2" -> islandsView().draw(out);

      // View all dashboards
      case "3" -> dashboardsView().draw(out);

      // View all character cards
      case "4" -> cloudsView().draw(out);

      // View my assistant cards
      case "5" -> new AssistantCardsView(me()).draw(out);

      // View my dashboard
      case "6" -> new DashboardView(me(), rules(), professorHolder()).draw(out);

      // Show turn orders
      case "7" -> {
        out.println("Plan order:");
        game().getPlanningPhaseOrder()
            .forEach(player -> out.print(player + " -> "));

        out.println();

        if (game().getGamePhase().equals(GamePhase.ACTION)) {
          out.println("Action order:");
          game().getActionPhaseOrder()
              .forEach(player -> out.print(player + " -> "));
        }
        out.println();
      }

      case "8" -> playersView().draw(out);

      // View all character cards
      case "10" -> {
        if (rules().gameMode.equals(GameMode.EXPERT))
          characterCardsView().draw(out);
      }
      default -> refreshMenu = false;
    }
    if (refreshMenu)
      showOptions();
  }

  /**
   * Handle common view options of the GameState
   *
   * @return True if deliberate disconnection is wanted, False otherwise
   */
  final protected boolean handleDisconnection(String choice) {
    if (choice.equals("0000")) {
      controller.sender().sendQuitGame();
      controller.fireChange(DELIBERATE_DISCONNECTION, null, null);
      return true;
    }
    return false;
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    // Force the return of the menus
    if (Arrays.asList(
            GAMEDATA_EVENT.tag,
            PLAYER_CONNECTION_CHANGED.tag,
            END_GAME.tag)
        .contains(evt.getPropertyName())) {
      InputHandler.get().setLine("forced_advancement_to_next_menu");
      inputGreenLight = true;
    }

    // Refresh view and print what's happened
    if (evt.getPropertyName().equals(GAMEDATA_EVENT.tag)) {
      String actionDescription = (String) evt.getNewValue();
      out.print("\n\n" + actionDescription, GREEN);
    }

    if (evt.getPropertyName().equals(PLAYER_CONNECTION_CHANGED.tag)) {
      out.println();
      out.println((String) evt.getNewValue());
    }
  }

  /**
   * Checks if I am amd the current player
   *
   * @return True if so, false otherwise.
   */
  protected boolean isMyTurn() {
    return game().isTurnOf(controller.getNickname());
  }
}
