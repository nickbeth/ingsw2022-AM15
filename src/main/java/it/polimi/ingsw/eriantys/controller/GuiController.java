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
    Platform.runLater(() -> Gui.showError(error));
  }
  @Override
  public void firePropertyChange(EventType event) {
    Platform.runLater(() -> listenerHolder.firePropertyChange(event.tag, null, null));
  }

  @Override
  public void run() {
    Application.launch(Gui.class);
  }

}
