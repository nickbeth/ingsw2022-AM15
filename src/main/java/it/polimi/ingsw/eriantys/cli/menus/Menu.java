
package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.InputHandler;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.EventType;
import org.fusesource.jansi.Ansi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.INPUT_ENTERED;
import static it.polimi.ingsw.eriantys.controller.EventType.INTERNAL_SOCKET_ERROR;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

/**
 * Base class for all menus.
 * Every menu shows a list of options and performs an operation based on the choice that has been made.
 */
public abstract class Menu implements PropertyChangeListener {
  protected static PrintStream out = System.out;
  protected static Scanner in = new Scanner(System.in);

  // Events that single menus want to listen to. Every menu listens to 'INTERNAL_SOCKET_ERROR' and 'INPUT_ENTERED'
  protected List<EventType> eventsToBeListening = new ArrayList<>(
      List.of(INTERNAL_SOCKET_ERROR, INPUT_ENTERED)
  );
  protected Controller controller = Controller.get();

  // Attribute used to block interaction until he receives a message
  protected volatile boolean greenLight = false;
  protected volatile boolean inputGreenLight = false;

  /**
   * Blocking method until a keyboard input arrives
   */
  protected String getKeyboardInput() {
    inputGreenLight = false;
    while (!inputGreenLight) {
      Thread.onSpinWait();
    }
    return InputHandler.get().getLine();
  }

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
  protected abstract void showOptions();

  /**
   * Shows a list of options and handles the selected choice.
   *
   * @return The next MenuEnum based on the decision made in this method
   */
  public abstract MenuEnum show() throws InterruptedException;

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    clientLogger.debug("Event arrived: " + evt.getPropertyName());
    if (evt.getPropertyName().equals(INPUT_ENTERED.tag)) {
      inputGreenLight = true;
      return;
    }
    if (evt.getPropertyName().equals(INTERNAL_SOCKET_ERROR.tag)) {
      clientLogger.error("Internal socket error occurred, server might be down");
    }
  }

  public List<EventType> getEventsToBeListening() {
    return eventsToBeListening;
  }

  final protected int getNumber() {
    String line;

    while (true) {
      try {
        line = getKeyboardInput();
        return Integer.parseInt(line);
      } catch (NumberFormatException e) {
        out.print("Must insert a number, insert again: ");
      }
    }
  }

  final protected String getNonBlankString() {
    String string;
    while (true) {
      string = getKeyboardInput();
      clientLogger.debug("getNonBlankString() String taken = " + string);
      if (!string.isBlank() && !string.contains(" ")) {
        clientLogger.debug("getNonBlankString() Returning string = " + string);
        return string;
      } else {
        clientLogger.debug("getNonBlankString() " + string + " is either empty or has spaces");
        out.print("Cannot neither be empty or contains spaces, insert again: ");
      }
    }
  }

  final protected void clearConsole() {
    // Clear screen
    out.print("\033[H\033[2J");
    out.println(Ansi.ansi().eraseScreen());
    out.flush();
  }
}
