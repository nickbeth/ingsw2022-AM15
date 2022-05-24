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

  public void start() {
  }

  public void finish() {
  }
}
