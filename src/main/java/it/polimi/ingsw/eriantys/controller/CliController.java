package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.MenuConnect;
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
  public void run() {
    super.run();
    Scanner in = new Scanner(System.in);
    Menu currentMenu = new MenuConnect(this);
    while (true) {
      currentMenu.show(in, out);
      currentMenu = currentMenu.next();
    }
  }
}
