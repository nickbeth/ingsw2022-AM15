package it.polimi.ingsw.eriantys.controller;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.cli.menus.lobby.MenuConnect;
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
  public void fireChanges(EventEnum event) {
  
  }
  
  @Override
  public void run() {
    Menu currentMenu = new MenuConnect(this);
    while (true) {
      currentMenu.show(in, out);
      currentMenu = currentMenu.next();
    }
  }
  
  
  
  
}
