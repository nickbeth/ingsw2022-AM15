package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.gui.Gui;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.network.Client;
import javafx.application.Application;
import javafx.application.Platform;

import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.GAMEINFO_EVENT;

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
  public void fireChanges(EventEnum event) {
    Platform.runLater(() -> listenerHolder.firePropertyChange(event.tag, null, null));
  }
  
  @Override
  public void run() {
    Gui.setController(this);
    Application.launch(Gui.class);
  }
  
  /**
   * Applies the given {@link GameAction} to the game state  and fires a property change.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if action was valid and was applied successfully, {@code false} otherwise
   */
  @Override
  public boolean executeAction(GameAction action) {
    return super.executeAction(action);
  }
}
