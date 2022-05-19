package it.polimi.ingsw.eriantys.client;

import org.tinylog.Logger;

public class Main {
  public static void main(String[] args) {
    System.out.println("Eriantys | 1.0 | AM15 | Client");
    boolean isGui = true;

    // Arguments parsing
    for (String arg : args) {
      switch (arg) {
        case "-c", "--cli" -> isGui = false;
        default -> Logger.warn("Unknown command line argument: {}", arg);
      }
    }

    ClientApp clientApp = new ClientApp(isGui);
    clientApp.run();
  }
}
