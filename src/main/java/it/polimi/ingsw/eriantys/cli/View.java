package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Base class for all views.
 */
public abstract class View {

  /**
   * Draws this view to an output stream.
   *
   * @param o The output stream which the view will write to.
   * @throws IOException If an I/O error occurs on the output stream.
   */
  abstract public void draw(PrintStream o) throws IOException;
}
