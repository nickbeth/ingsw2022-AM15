package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.InputHandler;
import it.polimi.ingsw.eriantys.cli.menus.MenuIterator;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.network.Client;
import org.fusesource.jansi.Ansi;

import java.io.PrintStream;
import java.util.Scanner;

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
    out.println(Ansi.ansi().fgRed().a("Please restart the application.").reset());
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
      preGame(iterator);
      inGame(iterator);
      if (gameState.getGamePhase().equals(GamePhase.WIN)) {
        out.println("Game ended");
        break;
      }
    }

    out.println("Closing application...");
  }

  private void inGame(MenuIterator iterator) {
    boolean escape = false;

    while (!escape) {
      if (iterator.menuAction()) {
        escape = true;
      }
    }
  }

  private void preGame(MenuIterator iterator) {
    while (!iterator.menuPreGame()) {
      iterator.goNext();
    }
  }
}

