package it.polimi.ingsw.eriantys.view;

import it.polimi.ingsw.eriantys.model.GameState;

import java.io.IOException;

/**
 * Base class for all views.
 */
public abstract class View {
  protected GameState gameState;

  /**
   * Draws this view to an output stream.
   *
   * @throws IOException If an I/O error occurs on the output stream.
   */
  abstract public void draw() throws IOException;
}
