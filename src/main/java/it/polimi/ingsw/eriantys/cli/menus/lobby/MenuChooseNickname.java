package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.NICKNAME_OK_EVENT;


public class MenuChooseNickname extends Menu {
  public MenuChooseNickname(CliController controller) {
    this.controller = controller;
    Logger.trace(controller.getListenerHolder().getPropertyChangeListeners().length);
    controller.addListener(this, NICKNAME_OK_EVENT.tag);
    Logger.trace(controller.getListenerHolder().getPropertyChangeListeners().length);
  }
  
  @Override
  protected void showOptions(PrintStream out) {
    out.print("Enter your nickname: ");
  }
  
  @Override
  public void show(Scanner in, PrintStream out) {
    String input;
    boolean done = false;
    
    do {
      greenLight = false;
      showOptions(out);
      input = in.nextLine();
      if (input.isBlank()) {
        out.println("A nickname cannot be empty or blank");
      } else {
        done = true;
        controller.sender().sendNickname(input);
        waitForGreenLight();
      }
    } while (!done);
  }
  
  @Override
  public Menu next() {
    return new MenuCreateOrJoin(controller);
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived");
    controller.removeListener(this);
    greenLight = true;
  }
}
