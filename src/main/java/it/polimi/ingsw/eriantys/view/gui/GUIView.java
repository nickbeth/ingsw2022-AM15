package it.polimi.ingsw.eriantys.view.gui;

import it.polimi.ingsw.eriantys.view.View;

import java.beans.PropertyChangeListener;

/**
 * Base class for GUI object implementation. It's responsible for showing the
 * updated GUI and reacts to the modifies of the GameState listening (implementing Listener)
 * to the invoker.
 */
public abstract class GUIView extends View implements PropertyChangeListener {
}
