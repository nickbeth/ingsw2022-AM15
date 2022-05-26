package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Base class for all menus.
 * Every menu shows a list of options and performs an operation based on the choice that has been made.
 */
public abstract class Menu implements PropertyChangeListener {
  protected CliController controller;
  protected Menu nextMenu;
  // Attribute used to block interaction until he receives a message
  protected boolean greenLight = false;

  /**
   * Blocking method until a message from server arrives
   */
  protected void waitForGreenLight() {
    while (!greenLight) {
      Thread.onSpinWait();
    }
  }

  /**
   * Shows the list of options this menu can handle.
   */
  protected abstract void showOptions(PrintStream out);

  final protected void showViewOptions(PrintStream out) {
    out.println("1 - View all");
    out.println("2 - View islands");
    out.println("3 - View dashboards");
    if (controller.getGameState().getRuleBook().gameMode.equals(GameMode.EXPERT))
      out.println("4 - CharacterCards");
    out.println("*** others ***");
  }

  final protected void showViewOptions(String choice, PrintStream out) {
    GameState game = controller.getGameState();
    RuleBook rule = game.getRuleBook();
    List<CharacterCard> ccs = game.getPlayingField().getCharacterCards();
    ProfessorHolder professorHolder = game.getPlayingField().getProfessorHolder();
    List<Island> islands = game.getPlayingField().getIslands();
    List<Dashboard> dashboards = game.getDashboards();
    int motherPosition = game.getPlayingField().getMotherNaturePosition();

    ViewGroup dashboardsView = new ViewGroup();
    dashboards.forEach(d ->
        dashboardsView.addView((new DashboardView(rule, d, professorHolder))));
    View islandsView = new IslandsView(islands, motherPosition);

    switch (choice) {
      case "1" -> (new ViewGroup()).addView(islandsView).addView(dashboardsView).draw(out);
      case "2" -> islandsView.draw(out);
      case "3" -> dashboardsView.draw(out);
      case "4" -> {
        if (controller.getGameState().getRuleBook().gameMode.equals(GameMode.EXPERT))
          (new CharacterCardView(ccs)).draw(out);
      }
      // Simply goes on
      default -> {}
    }
  }


  /**
   * Shows a list of options and handles the selected choice.
   *
   * @param in  The input stream the user input will be read from
   * @param out The output stream the output will be sent to
   */
  public abstract void show(Scanner in, PrintStream out);

  /**
   * Returns the next menu to be shown after the current one.
   *
   * @return The next menu
   */
  final public Menu next() {
    return nextMenu;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived" + evt.getPropertyName());
    greenLight = true;
    controller.removeListener(this);
  }

  final protected int getNumber(Scanner in, PrintStream out) {
    String number;
    while (true) {
      try {
        number = in.nextLine();
        return Integer.parseInt(number);
      } catch (NumberFormatException e) {
        out.print("Must insert a number, insert again: ");
      }
    }
  }

  final protected String getNonBlankString(Scanner in, PrintStream out) {
    String string;
    while (true) {
      string = in.nextLine();
      if (!string.isBlank())
        return string;
      else
        out.print("Cannot be Empty, insert again: ");
    }
  }
}
