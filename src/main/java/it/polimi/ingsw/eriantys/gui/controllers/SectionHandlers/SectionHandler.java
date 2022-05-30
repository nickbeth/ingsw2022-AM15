package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

public abstract class SectionHandler {
  protected boolean created = false;

  public void update() {
    if (created) refresh();
    else create();
  }

  protected void refresh() {
  }

  /**
   * Sets created flag to true
   */
  protected void create() {
    created = true;
  }
}
