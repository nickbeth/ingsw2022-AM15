package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.client.MessageHandler;
import it.polimi.ingsw.eriantys.network.Client;

public class GuiController extends Controller implements Runnable {
  public GuiController(Client networkClient, MessageHandler messageHandler) {
    super(networkClient, messageHandler);
  }

  @Override
  public void run() {

  }
}
