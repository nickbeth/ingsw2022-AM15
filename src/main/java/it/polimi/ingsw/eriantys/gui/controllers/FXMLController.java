package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.Gui;

public abstract class FXMLController {
  protected Gui gui;
  public void setGui(Gui gui){
    this.gui = gui;
  }
}
