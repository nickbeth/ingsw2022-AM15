package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.network.Client;
import javafx.application.Application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GuiController extends Controller {
  private Gui gui;

  public GuiController(Client networkClient) {
    super(networkClient);
  }

  public void setGui(Gui gui) {
    this.gui = gui;
  }

  @Override
  public void showError(String error) {
    gui.showError(error);
  }

  @Override
  public void run() {
    super.run();
    Gui.setController(this);
    Application.launch(Gui.class);
  }

}
