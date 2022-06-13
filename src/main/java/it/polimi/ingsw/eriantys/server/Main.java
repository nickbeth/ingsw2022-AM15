package it.polimi.ingsw.eriantys.server;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;

public class Main {
  public static void main(String[] args) {
    System.out.println("Eriantys | 1.0 | AM15 | Server");

    ServerArgs serverArgs = new ServerArgs();

    // Arguments parsing
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-p", "--port" -> {
          try {
            serverArgs.port = Integer.parseInt(args[i + 1]);
            i++;
          } catch (RuntimeException ignored) {
            System.out.println("Port argument was specified without a valid port, default (" + serverArgs.port + ") will be used instead");
          }
        }
        case "--no-heartbeat" -> serverArgs.heartbeat = false;
        case "-h", "--help" -> {
          System.out.println("Usage: java -jar eriantys-server.jar [-p <port>] [--no-heartbeat]");
          System.exit(0);
        }
        default -> serverLogger.info("Unknown command line argument: {}", args[i]);
      }
    }

    ServerApp serverApp = new ServerApp(serverArgs);
    serverApp.run();
  }
}
