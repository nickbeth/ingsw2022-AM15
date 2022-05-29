package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * End game display
 */
public class MenuEndGame extends MenuGame{
  public MenuEndGame() {
    super();
  }

  @Override
  protected void showOptions(PrintStream out) {
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {
    return null;
  }
}
