package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.controller.Controller;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Base class for all menus.
 * Every menu shows a list of options and performs an operation based on the choice that has been made.
 */
public abstract class Menu {
  protected Controller controller;

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
}
