package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

public abstract class SectionHandler {
  private boolean created = false;

  /**
   * If {@link #created} is false calls {@link #create()} method.<br>
   * Then calls {@link #refresh()} method.
   */
  public void update() {
    if (!created) {
      create();
    }
    refresh();
  }

  /**
   * Base implementation does nothing
   */
  protected void refresh() {
  }

  /**
   * Sets created flag to true
   */
  protected void create() {
    created = true;
  }
}
