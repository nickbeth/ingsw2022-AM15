package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuConnect;
import it.polimi.ingsw.eriantys.network.Client;
import org.fusesource.jansi.Ansi;

public class CliController extends Controller implements Runnable {
  public CliController(Client networkClient) {
    super(networkClient);
  }

  @Override
  public void showError(String error) {
    System.out.println(Ansi.ansi().fgRed().a(error).reset());
  }

  @Override
  public void run() {
    Menu currentMenu = new MenuConnect(this);
    while (true) {
      currentMenu.makeChoice();
      currentMenu = currentMenu.nextMenu();
    }
  }
}
