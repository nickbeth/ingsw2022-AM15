package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;


public class ObservableActionInvoker extends ActionInvoker {
  // Attributes that manages the listeners
  private PropertyChangeSupport support;

  public ObservableActionInvoker(GameState gameState) {
    super(gameState);
    support = new PropertyChangeSupport(this);
  }

  /**
   * Adds a listener (observer) to the event "Action executed"
   * @param newListener The new listener wanted to be added
   */
  public void addListener(PropertyChangeListener newListener) {
    support.addPropertyChangeListener(newListener);
  }

  /**
   * Removes the given listener
   * @param listener Listener wanted to be removed
   */
  public void removeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }

  /**
   * Execute the Game Action passed as parameter
   * @param action The Action which modifies the gameState
   * @return True if action is valid and succeed. False otherwise.
   */
  @Override
  public boolean executeAction(GameAction action) {
    if (action.isValid(gameState)) {
      action.apply(gameState);
      gameActions.add(action);

      // Notifies listeners of property changes
      support.firePropertyChange(null);

      return true;
    }
    return false;
  }
}
