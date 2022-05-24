package it.polimi.ingsw.eriantys.cli.views;

import java.io.PrintStream;

/**
 * Base class for all views.
 */
public abstract class View {

  /**
   * Draws this view to an output stream.
   *
   * @param o The output stream which the view will write to.
   */
  abstract public void draw(PrintStream o);
}
