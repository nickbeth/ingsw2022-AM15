package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;

/**
 * Abstract class dedicated to the management of the input interaction
 */
public abstract class Menu {
  protected GameState game;
  protected String playerNickname;
  protected Controller controller;

  /**
   * Shows the options the player can make
   */
  protected abstract void showOptions();

  /**
   * Control logic of the choice of the player
   */
  protected abstract Input.InputBuilder makeChoice();

  /**
   *
   */
  protected abstract Menu nextMenu();
}
