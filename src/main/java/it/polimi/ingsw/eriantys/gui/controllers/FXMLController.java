package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;

import java.beans.PropertyChangeListener;

public abstract class FXMLController implements PropertyChangeListener {
  protected Gui gui;
  public void setGui(Gui gui){
    this.gui = gui;
  }
}
