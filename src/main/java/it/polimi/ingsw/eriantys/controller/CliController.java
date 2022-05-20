package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.controller.menus.action.MenuConnect;
import it.polimi.ingsw.eriantys.network.Client;

public class CliController extends Controller implements Runnable {
  public CliController(Client networkClient) {
    super(networkClient);
  }

  @Override
  public void run() {
    Menu currentMenu = new MenuConnect(this);
    while (true) {
      currentMenu.makeChoice(new ParamBuilder());
      currentMenu = currentMenu.nextMenu();
    }
  }
}
