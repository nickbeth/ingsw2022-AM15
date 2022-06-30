package it.polimi.ingsw.eriantys.client;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class Main {
  public static void main(String[] args) {
    System.out.println("Eriantys | 1.0 | AM15 | Client");
    boolean isGui = true;

    // Arguments parsing
    for (String arg : args) {
      switch (arg) {
        case "-c", "--cli" -> isGui = false;
        default -> clientLogger.warn("Unknown command line argument: {}", arg);
      }
    }

    ClientApp clientApp = new ClientApp(isGui);
    clientApp.run();
  }
}
