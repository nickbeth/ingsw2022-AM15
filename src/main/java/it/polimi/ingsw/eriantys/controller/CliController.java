package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.network.Client;

public class CliController extends Controller {
  public CliController(Client client, GameInfo info) {
    super(client, info);
  }
}
