package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.views.*;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.EventType;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
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
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.INTERNAL_SOCKET_ERROR;
import static java.lang.System.out;

/**
 * Base class for all menus.
 * Every menu shows a list of options and performs an operation based on the choice that has been made.
 */
public abstract class Menu implements PropertyChangeListener {
  // Events that single menus want to listen to. Every menus listent to 'INTERNAL_SOCKET_ERROR'
  protected List<EventType> eventsToBeListening = new ArrayList<>(
      List.of(INTERNAL_SOCKET_ERROR)
  );
  protected Controller controller = Controller.getController();
  // Attribute used to block interaction until he receives a message
  protected volatile boolean greenLight = false;


  /**
   * Blocking method until a message from server arrives
   */
  protected void waitForGreenLight() {
    greenLight = false;
    while (!greenLight) {
      Thread.onSpinWait();
    }
  }

  /**
   * Shows the list of options this menu can handle.
   */
  protected abstract void showOptions(PrintStream out);

  /**
   * Shows a list of options and handles the selected choice.
   *
   * @param in  The input stream the user input will be read from
   * @param out The output stream the output will be sent to
   * @return The next MenuEnum based on the decision made in this method
   */
  public abstract MenuEnum show(Scanner in, PrintStream out);

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(INTERNAL_SOCKET_ERROR.tag)) {
      Logger.error("Internal socket error occured, server might be down");
    }
  }

  public List<EventType> getEventsToBeListening() {
    return eventsToBeListening;
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
      if (!string.isBlank() && !string.contains(" "))
        return string;
      else
        out.print("Cannot neither be empty or contains spaces, insert again: ");
    }
  }

  final protected void clearConsole(){
    // Clear screen
    out.print("\033[H\033[2J");
    out.flush();
  }
}
