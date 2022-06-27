package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;
import it.polimi.ingsw.eriantys.cli.menus.game.*;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuChooseNickname;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuConnect;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuCreateOrJoin;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuLobby;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import static it.polimi.ingsw.eriantys.cli.CustomPrintStream.out;
import static it.polimi.ingsw.eriantys.cli.menus.MenuEnum.*;
import static it.polimi.ingsw.eriantys.cli.menus.MenuIterator.MenuFactory.makeMenu;
import static it.polimi.ingsw.eriantys.controller.EventType.DELIBERATE_DISCONNECTION;
import static it.polimi.ingsw.eriantys.controller.EventType.START_GAME;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;

public class MenuIterator implements PropertyChangeListener {
  private final Controller controller = Controller.get();
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
        case EFFECT -> {
          return new MenuEffect();
        }
        case END -> {
          return new MenuEndGame();
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
    controller.addListener(this, START_GAME.tag);
    controller.addListener(this, DELIBERATE_DISCONNECTION.tag);

    // Setting starting menu
    currentMenu = new MenuConnect();
  }

  /**
   * Handle PreGame menus
   *
   * @return True if a game is started.
   */
  public boolean menuPreGame() {
    addEventsToBeListened(currentMenu);
    try {
      nextMenu = currentMenu.show();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    removeEventsToBeListened(currentMenu);

    // Case Exit game
    if (nextMenu == null) {
      return true;
    }

    return Arrays.asList(
        PICK_ASSISTANT,
        PLACING,
        EFFECT,
        MOVING,
        PICKING_CLOUD).contains(nextMenu);
  }

  /**
   * Handle InGame menus
   *
   * @return True if a game is ended or the player disconnects.
   */
  public boolean menuAction() {
    currentMenu = currentGameMenu();
    addEventsToBeListened(currentMenu);
    try {
      MenuEnum nextPossibleMenu = currentMenu.show();
      if (nextPossibleMenu != null) {
        if (nextPossibleMenu.equals(CREATE_OR_JOIN)) {
          currentMenu = makeMenu(CREATE_OR_JOIN);
          return true;
        }
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    removeEventsToBeListened(currentMenu);

    return false;
  }

  private Menu currentGameMenu() {
    GameState game = controller.getGameState();
    switch (game.getGamePhase()) {
      case PLANNING -> {
        return makeMenu(MenuEnum.PICK_ASSISTANT);
      }
      case ACTION -> {
        switch (game.getTurnPhase()) {
          case PLACING -> {
            return makeMenu(MenuEnum.PLACING);
          }
          case MOVING -> {
            return makeMenu(MenuEnum.MOVING);
          }
          case PICKING -> {
            return makeMenu(MenuEnum.PICKING_CLOUD);
          }
        }
      }
      // Todo: test WIN screen update
      case WIN -> {
        return makeMenu(MenuEnum.END);
      }
    }
    return makeMenu(CONNECTION);
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

  // Set the current menu as listener of its list of events

  private void addEventsToBeListened(Menu menu) {
    menu.getEventsToBeListening().forEach(eventType ->
        controller.addListener(menu, eventType.tag)
    );
  }
  // Removes the current menu as listener of its list of events

  private void removeEventsToBeListened(Menu menu) {
    menu.getEventsToBeListening().forEach(eventType ->
        controller.removeListener(menu, eventType.tag)
    );
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(DELIBERATE_DISCONNECTION.tag)) {
      out.print("\nDisconnected.", YELLOW);
    }
  }

  public void setCurrentMenu(MenuEnum menu) {
    this.currentMenu = makeMenu(menu);
  }

  public MenuEnum getNextMenu() {
    return nextMenu;
  }
}
