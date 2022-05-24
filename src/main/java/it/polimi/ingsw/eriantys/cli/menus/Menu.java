package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.controller.CliController;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Base class for all menus.
 * Every menu shows a list of options and performs an operation based on the choice that has been made.
 */
public abstract class Menu implements PropertyChangeListener {
  protected CliController controller;
  // Attribute used to block interaction until he receives a message
  protected boolean greenLight = false;
  
  protected void waitForGreenLight() {
    while (!greenLight) {
      Thread.onSpinWait();
    }
  }
  
  /**
   * Shows the list of options this menu can handle.
   */
  protected abstract void showOptions(PrintStream out);
  
  public void showViewOptions() {
    System.out.println("1 - View dashboards");
    System.out.println("2 - View islands");
    System.out.println("*** others ***");
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
  public abstract Menu next();
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived");
    greenLight = true;
    controller.removeListener(this);
  }
}
