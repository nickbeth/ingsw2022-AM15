package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;

public interface Menu {
  // todo void draw([View view]?);
  void commandMenu(ActionInvoker invoker, String playerNickname, GameState gameState);
}
