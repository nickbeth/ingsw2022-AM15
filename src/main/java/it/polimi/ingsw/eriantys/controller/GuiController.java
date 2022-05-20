package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.network.Client;

public class GuiController extends Controller implements Runnable {
  public GuiController(Client networkClient) {
    super(networkClient);
  }

  @Override
  public void run() {

  }
}
