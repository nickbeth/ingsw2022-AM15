package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

public abstract class SectionHandler {
  private boolean created = false;

  /**
   * If {@link #created} is false calls {@link #create()} method.<br>
   * Then calls {@link #refresh()} method.
   */
  public void update() {
    if (!created) {
      create();
      created = true;
    }
    refresh();
  }

  /**
   * Base implementation does nothing
   */
  protected void refresh() {
  }

  /**
   * Base implementation does nothing
   */
  protected void create() {
  }
}
