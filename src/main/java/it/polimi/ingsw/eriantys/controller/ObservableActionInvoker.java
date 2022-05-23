package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableActionInvoker extends ActionInvoker {
  /**
   * Stores the listeners that subscribed for notification when an action is applied.
   */
  private final PropertyChangeSupport support;

  public ObservableActionInvoker(GameState gameState) {
    super(gameState);
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * Subscribe a listener for notification when an action is applied.
   *
   * @param listener The listener to be subscribed
   */
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes the given listener.
   *
   * @param listener Listener to be unsubscribed
   */
  public void removeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }

  public void removeAllListener() {
    //TODO: remove all listeners
  }

  public void firePropertyChange() {
    support.firePropertyChange(null);
  }
  /**
   * Applies the given {@link GameAction} to the game state and notifies all subscribed listeners.
   *
   * @param action The {@link GameAction} to apply to the game state
   * @return {@code true} if action was valid and was applied successfully, {@code false} otherwise
   */
  @Override
  public boolean executeAction(GameAction action) {
    if (!super.executeAction(action))
      return false;
    // Notifies listeners that an action was applied
    support.firePropertyChange(null);
    return true;
  }
}
