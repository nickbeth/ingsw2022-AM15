package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.beans.PropertyChangeSupport;

import static it.polimi.ingsw.eriantys.controller.Controller.GAMEDATA_EVENT_TAG;

public class ObservableActionInvoker extends ActionInvoker {
  /**
   * Stores the listeners that subscribed for notification when an action is applied.
   */
  private final PropertyChangeSupport listenerHolder;

  public ObservableActionInvoker(GameState gameState, PropertyChangeSupport listenerHolder) {
    super(gameState);
    this.listenerHolder = listenerHolder;
  }

  /**
   * Applies the given {@link GameAction} to the game state and fires a property change.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if action was valid and was applied successfully, {@code false} otherwise
   */
  @Override
  public boolean executeAction(GameAction action) {
    if (!super.executeAction(action))
      return false;
    // Notifies listeners that the game state was modified
    listenerHolder.firePropertyChange(GAMEDATA_EVENT_TAG, null, null);
    return true;
  }
}
