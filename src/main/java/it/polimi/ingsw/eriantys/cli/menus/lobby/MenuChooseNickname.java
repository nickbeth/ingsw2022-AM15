package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.controller.EventType.ERROR;
import static it.polimi.ingsw.eriantys.controller.EventType.NICKNAME_OK;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;


public class MenuChooseNickname extends Menu {
  private boolean isNicknameOk = false;

  public MenuChooseNickname() {
    eventsToBeListening.add(NICKNAME_OK);
    eventsToBeListening.add(ERROR);
  }

  @Override
  protected void showOptions() {
    out.println("\n1 - Enter your nickname: ");
    out.println("ENTER - Use default nickname \"Baolo\": ");
    out.println("0 - Back");
  }

  @Override
  public synchronized MenuEnum show() throws InterruptedException {
    String nickname;
    String choice;

    while (true) {
      showOptions();
      out.print("Make a choice: ");
      choice = getKeyboardInput();
      clientLogger.debug("Handling choice");
      switch (choice) {

        // Default nickname
        case "" -> {
          isNicknameOk = false;

          controller.sender().sendNickname("Baolo");
          waitForGreenLight();
          if (isNicknameOk)
            return MenuEnum.CREATE_OR_JOIN;
        }

        // Choose nickname
        case "1" -> {
          isNicknameOk = false;

          out.print("Insert nickname: ");
          nickname = getNonBlankString();
          controller.sender().sendNickname(nickname);

          waitForGreenLight();
          if (isNicknameOk)
            return MenuEnum.CREATE_OR_JOIN;
        }
        // Go back
        case "0" -> {
          return MenuEnum.CONNECTION;
        }
        default -> out.println("Choose a valid option");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    if (evt.getPropertyName().equals(NICKNAME_OK.tag)) {
      clientLogger.debug("Message from server. Nickname is ok");
      isNicknameOk = true;
    }
    greenLight = true;
  }
}
