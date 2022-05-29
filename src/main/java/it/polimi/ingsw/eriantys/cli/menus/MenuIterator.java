package it.polimi.ingsw.eriantys.cli.menus;

import it.polimi.ingsw.eriantys.cli.menus.game.*;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuChooseNickname;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuConnect;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuCreateOrJoin;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuLobby;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.EventType;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.cli.menus.MenuIterator.MenuFactory.makeMenu;
import static it.polimi.ingsw.eriantys.controller.Controller.getController;
import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;

public class MenuIterator implements Iterator<Menu>, PropertyChangeListener {
  private Menu currentMenu;
  private MenuEnum nextMenu;
  private Controller controller = getController();

  static class MenuFactory {
    /**
     * Methods responsible of creating menus
     *
     * @param menuType
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
          Logger.error(new IllegalArgumentException(), "Passed a not valid argument");
          return null;
        }
      }
    }
  }

  public MenuIterator() {
    controller.addListener(this, GAMEDATA_EVENT.tag);
    // Setting starting menu
    currentMenu = new MenuConnect();
  }

  public void menuAction(Scanner in, PrintStream out) {
    addListeners();
    nextMenu = currentMenu.show(in, out);
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

  @Override
  public Menu next() {
    currentMenu = makeMenu(nextMenu);
    return currentMenu;
  }

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(EventType.GAME_ENDED.tag)) {
      System.out.println("GAME_ENDED");
      new MenuEndGame().show(new Scanner(System.in), System.out);
    }
  }
}
