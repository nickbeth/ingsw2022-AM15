package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.controller.CliController;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.ERROR;
import static it.polimi.ingsw.eriantys.controller.EventType.NICKNAME_OK;


public class MenuChooseNickname extends Menu {
  private boolean isNicknameOk = false;

  public MenuChooseNickname() {
    eventsToBeListening.add(NICKNAME_OK);
    eventsToBeListening.add(ERROR);
  }

  @Override
  protected void showOptions(PrintStream out) {
    out.println("\n1 - Enter your nickname: ");
    out.println("0 - Back");
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {
    String nickname;

    while (true) {

      showOptions(out);
      out.print("Make a choice: ");

      switch (in.nextLine()) {
        case "1" -> {
          isNicknameOk = false;

          out.print("Insert nickname: ");
          nickname = getNonBlankString(in, out);
          controller.sender().sendNickname(nickname);

          waitForGreenLight();
          if (isNicknameOk)
            return MenuEnum.CREATE_OR_JOIN;
        }
        // Go back
        case "0" -> {
          waitForGreenLight();
          return MenuEnum.CONNECTION;
        }
        default -> out.println("Choose a valid option");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Logger.trace("Response arrived");
    super.propertyChange(evt);
    if (evt.getPropertyName().equals(NICKNAME_OK.tag)) {
      isNicknameOk = true;
    }
    greenLight = true;
  }
}
