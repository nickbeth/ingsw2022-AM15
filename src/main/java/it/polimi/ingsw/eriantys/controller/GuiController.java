package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.network.Client;
import javafx.application.Application;
import javafx.application.Platform;

public class GuiController extends Controller {

  public GuiController(Client networkClient) {
    super(networkClient);
  }

  @Override
  public void showError(String error) {
    //TODO: handle error messages in gui with listener
  }
  
  @Override
  public void fireChanges(EventEnum event) {
    Platform.runLater(() -> listenerHolder.firePropertyChange(event.tag, null, null));
  }
  
  @Override
  public void run() {
    Gui.setController(this);
    Application.launch(Gui.class);
  }
  
}
