package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.GameState;

/**
 * Abstract class dedicated to the management of the input interaction
 */
public abstract class Menu {
  protected GameState game;
  protected String playerNickname;
  protected Controller controller;
  protected GameInfo gameInfo;
  /**
   * Shows the options the player can make
   */
  public abstract void showOptions();

  public void showViewOptions() {
    System.out.println("1 - View dashboards");
    System.out.println("2 - View islands");
    System.out.println("*** others ***");
  }

  /**
   * Control logic of the choice of the player
   * @param paramBuilder
   */
  public abstract void makeChoice(ParamBuilder paramBuilder);


  public abstract Menu nextMenu();
}
