package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.menus.MenuIterator;
import it.polimi.ingsw.eriantys.network.Client;
import org.fusesource.jansi.Ansi;

import java.io.PrintStream;
import java.util.Scanner;

public class CliController extends Controller {
  PrintStream out = System.out;
  Scanner in = new Scanner(System.in);

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
    MenuIterator menus = new MenuIterator();
    while (true) {
      menus.menuAction();
      menus.goNext();
    }
  }
}

