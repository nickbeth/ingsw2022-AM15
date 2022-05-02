package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.Server;
import org.tinylog.Logger;

public class Main {
  public static void main(String[] args) {
    System.out.println("Eriantys | 1.0 | AM15 | Server");

    int port = Server.DEFAULT_PORT;

    // Arguments parsing
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-p", "--port" -> {
          try {
            port = Integer.parseInt(args[i + 1]);
            i++;
          } catch (RuntimeException ignored) {
            System.out.println("Port argument was specified without a valid port, default (" + port + ") will be used instead");
          }
        }
        default -> Logger.info("Unknown command line argument: {}", args[i]);
      }
    }

    ServerApp serverApp = new ServerApp(port);
    serverApp.run();
  }
}
