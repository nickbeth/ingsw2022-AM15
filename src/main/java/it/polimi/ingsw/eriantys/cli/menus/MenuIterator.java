package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.InputHandler;
import it.polimi.ingsw.eriantys.cli.menus.game.*;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuChooseNickname;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuConnect;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuCreateOrJoin;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuLobby;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.EventType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static it.polimi.ingsw.eriantys.cli.menus.MenuIterator.MenuFactory.makeMenu;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class MenuIterator implements PropertyChangeListener {
  private final Controller controller = Controller.get();
  private InputHandler inputHandler = InputHandler.get();
  private MenuEnum nextMenu;
  private Menu currentMenu;

  static class MenuFactory {
    /**
     * Methods responsible for creating menus
     *
     * @param menuType MenuEnum wanted to be created
     * @return The Menu corresponding to the given type
     */
    static Menu makeMenu(MenuEnum menuType) {
      switch (menuType) {
        case CONNECTION -> {
          return new MenuConnect();
        }
        case NICKNAME -> {
          return new MenuChooseNickname();
        }
        case CREATE_OR_JOIN -> {
          return new MenuCreateOrJoin();
        }
        case LOBBY -> {
          return new MenuLobby();
        }
        case PICK_ASSISTANT -> {
          return new MenuPickAssistantCard();
        }
        case PLACING -> {
          return new MenuPlacing();
        }
        case MOVING -> {
          return new MenuMoving();
        }
        case PICKING_CLOUD -> {
          return new MenuPickingCloud();
        }
        default -> {
          clientLogger.error("Passed a not valid argument");
          return null;
        }
      }
    }
  }

  public MenuIterator() {
    // Setting events MenuIterator has to listen to
    controller.addListener(this, EventType.START_GAME.tag);

    // Set common event to listen to
//    controller.addListener(this, GAMEDATA_EVENT.tag);

    // Starting input handler
    new Thread(inputHandler, "InputHandler").start();

    // Setting starting menu
    currentMenu = new MenuConnect();
  }

  public void menuAction() {
    addListeners();
    try {
      nextMenu = currentMenu.show();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    removeListeners();
  }

  private void addListeners() {
    currentMenu.getEventsToBeListening().forEach(eventType ->
        controller.addListener(currentMenu, eventType.tag)
    );
  }

  private void removeListeners() {
    currentMenu.getEventsToBeListening().forEach(eventType ->
        controller.removeListener(currentMenu, eventType.tag)
    );
  }

  /**
   * Move on the usage of the menus
   *
   * @return The next menu
   */
  public MenuEnum goNext() {
    currentMenu = makeMenu(nextMenu);
    return nextMenu;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(EventType.GAME_ENDED.tag)) {
      System.out.println("GAME_ENDED");
      new MenuEndGame().show();
    }
  }
}
