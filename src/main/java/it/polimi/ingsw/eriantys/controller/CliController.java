package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.InputHandler;
import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.menus.MenuIterator;
import it.polimi.ingsw.eriantys.cli.menus.game.MenuEndGame;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.network.Client;
import org.fusesource.jansi.Ansi;

import java.io.PrintStream;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.GREEN;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;

public class CliController extends Controller {
  PrintStream out = System.out;

  public CliController(Client networkClient) {
    super(networkClient);
  }

  @Override
  public void showError(String error) {
    out.println(Ansi.ansi().fgRed().a(error).reset());
  }

  @Override
  public void showNetworkError(String error) {
    out.println(Ansi.ansi().fgRed().a(error).reset());
  }

  @Override
  public void fireChange(EventType event, Object oldValue, Object newValue) {
    listenerHolder.firePropertyChange(event.tag, null, newValue);
  }

  @Override
  public void run() {
    MenuIterator iterator = new MenuIterator();
    // Starting input handler
    Thread inputHandler = new Thread(InputHandler.get(), "InputHandler");
    inputHandler.setDaemon(true);
    inputHandler.start();

    while (true) {
      if (preGame(iterator)) {
        out.println(colored("Closing application...", YELLOW));
        break;
      }
      inGame(iterator);
    }

    out.println(colored("Application has terminated normally", GREEN));
  }

  private void inGame(MenuIterator iterator) {
    boolean escape = false;

    while (!escape) {
      if (iterator.menuAction()) {
        escape = true;
      }
    }
//    if (gameState.getGamePhase().equals(GamePhase.WIN)) {
//      new MenuEndGame().show();
//      iterator.setCurrentMenu(MenuEnum.CREATE_OR_JOIN);
//    }
  }

  /**
   * Iterates pre game menu based on user's choices
   *
   * @return True if a game is started. False if the user wants to end the application
   */
  private boolean preGame(MenuIterator iterator) {
    while (!iterator.menuPreGame()) {
      if (iterator.getNextMenu() != null)
        iterator.goNext();
    }
    return iterator.getNextMenu() == null;
  }
}

