package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.util.Optional;
import java.util.Stack;

import static it.polimi.ingsw.eriantys.controller.EventType.*;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;


public class MenuChooseNickname extends Menu {
  private boolean isNicknameOk = false;
  private boolean gameAlreadyStarted = false;

  private GameState game() {
    return controller.getGameState();
  }

  public MenuChooseNickname() {
    eventsToBeListening.add(NICKNAME_OK);
    eventsToBeListening.add(START_GAME);
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

          if (isNicknameOk) {
            if (!gameAlreadyStarted)
              return MenuEnum.CREATE_OR_JOIN;
            else {
              out.println("Loading the game...");
              waitForGreenLight();

              return currentMenu();
            }
          }
        }

        // Choose nickname
        case "1" -> {
          isNicknameOk = false;

          out.print("Insert nickname: ");
          nickname = getNonBlankString();
          controller.sender().sendNickname(nickname);

          waitForGreenLight();

          if (isNicknameOk) {
            if (!gameAlreadyStarted)
              return MenuEnum.CREATE_OR_JOIN;
            else {
              out.println("You were already playing. Do you want to connect to the previous game?");
              out.println("ANY_KEY - Yes");
              out.println("N - No");

              if (getNonBlankString().equalsIgnoreCase("n"))
                break;

              out.println("Loading the game...");
              waitForGreenLight();

              return currentMenu();
            }
          }
        }
        // Go back
        case "0" -> {
          controller.disconnect();
          return MenuEnum.CONNECTION;
        }
        default -> out.print("Choose a valid option: ");
      }
    }
  }

  private MenuEnum currentMenu() {
    TurnPhase turnPhase = game().getTurnPhase();
    GamePhase gamePhase = game().getGamePhase();

    if (gamePhase.equals(GamePhase.PLANNING)) return MenuEnum.PICK_ASSISTANT;

    if (turnPhase.equals(TurnPhase.PLACING)) return MenuEnum.PLACING;
    if (turnPhase.equals(TurnPhase.MOVING)) return MenuEnum.MOVING;
    if (turnPhase.equals(TurnPhase.PICKING)) return MenuEnum.PICKING_CLOUD;

    return null;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);

    if (evt.getPropertyName().equals(NICKNAME_OK.tag)) {
      clientLogger.debug("Message from server. Nickname is ok");
      isNicknameOk = true;
      if (evt.getNewValue() != null) {
        out.println("You were already in a game.");
        gameAlreadyStarted = true;
      }
    }

    greenLight = true;
  }


}
