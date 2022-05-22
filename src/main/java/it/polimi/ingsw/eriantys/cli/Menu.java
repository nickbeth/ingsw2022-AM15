package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.controller.Controller;

/**
 * Abstract class dedicated to the management of the input interaction
 */
public abstract class Menu {
  protected Controller controller;

  /**
   * Shows the options the player can make
   */
  protected abstract void showOptions();

  public void showViewOptions() {
    System.out.println("1 - View dashboards");
    System.out.println("2 - View islands");
    System.out.println("*** others ***");
  }

  /**
   * Control logic of the choice of the player
   */
  public abstract void makeChoice();


  public abstract Menu nextMenu();
}
