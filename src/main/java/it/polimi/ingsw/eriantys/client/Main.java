package it.polimi.ingsw.eriantys.client;

import org.tinylog.Logger;

public class Main {
  public static void main(String[] args) {
    System.out.println("Eriantys | 1.0 | AM15 | Client");
    boolean isGui;

    // Arguments parsing
    for (String arg : args) {
      switch (arg) {
        case "-g", "--gui" -> isGui = true;
        default -> Logger.warn("Unknown command line argument: {}", arg);
      }
    }

    ClientApp clientApp = new ClientApp();
    clientApp.run();
  }
}
