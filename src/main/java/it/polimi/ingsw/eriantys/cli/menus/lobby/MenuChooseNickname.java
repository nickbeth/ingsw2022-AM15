package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.ERROR;
import static it.polimi.ingsw.eriantys.controller.EventType.NICKNAME_OK;


public class MenuChooseNickname extends Menu {
  private boolean isNicknameOk = false;

  public MenuChooseNickname(CliController controller) {
    this.controller = controller;
    this.nextMenu = new MenuCreateOrJoin(controller);
    controller.addListener(this, NICKNAME_OK.tag);
    controller.addListener(this, ERROR.tag);
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.println("\n1 - Enter your nickname: ");
    out.println("0 - Back");
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    String nickname;

    do {
      showOptions(out);
      out.print("Make a choice: ");
      switch (in.nextLine()) {
        case "1" -> {
          isNicknameOk = false;
          greenLight = false;
          out.print("Insert nickname: ");
          nickname = getNonBlankString(in, out);
          controller.sender().sendNickname(nickname);
          waitForGreenLight();
        }
        case "0" -> {
          nextMenu = new MenuConnect(controller);
          return;
        }
        default -> out.println("Choose a valid option");
      }
    } while (!isNicknameOk);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived");
    if (evt.getPropertyName().equals(NICKNAME_OK.tag)) {
      isNicknameOk = true;
      controller.removeListener(this, NICKNAME_OK.tag);
      controller.removeListener(this, ERROR.tag);
    }
    greenLight = true;
  }
}
