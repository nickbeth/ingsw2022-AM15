package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;

/**
 * Base class for all views.
 */
public abstract class View {

  /**
   * Draws this view to an output stream.
   *
   * @param o The output stream which the view will write to.
   */
  abstract public void draw(CustomPrintStream o);
}
