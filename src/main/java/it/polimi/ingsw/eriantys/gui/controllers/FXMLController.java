package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;

import java.beans.PropertyChangeListener;

public abstract class FXMLController implements PropertyChangeListener {
  protected Gui gui;
  public void setGui(Gui gui){
    this.gui = gui;
  }

  /**
   * Method that updates all entities in a certain scene.<br>
   * The implementation of this method provided by the FXMLController class does nothing.
   */
  public void updateAll() {
  }

  /**
   * Method that's called at the beginning of a Scene Controller life cycle
   * The implementation of this method provided by the FXMLController class does nothing.
   */
  public void start() {
  }

  /**
   * Method that's called at the end of a Scene Controller life cycle
   * The implementation of this method provided by the FXMLController class does nothing.
   */
  public void finish() {
  }
}
