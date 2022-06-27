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
            serverLogger.warn("Port argument was specified without a valid port, default {} will be used instead", serverArgs.port);
          }
        }
        case "--no-heartbeat" -> serverArgs.heartbeat = false;
        case "-d", "--delete-timeout" -> {
          try {
            serverArgs.deleteTimeout = Integer.parseInt(args[i + 1]);
            i++;
          } catch (RuntimeException ignored) {
            serverLogger.warn("Delete timeout argument was specified without a valid value, default {} will be used instead", serverArgs.deleteTimeout);
          }
        }
        case "-h", "--help" -> {
          System.out.println("Usage: java -jar eriantys-server.jar [-p <port>] [--no-heartbeat]");
          System.exit(0);
        }
        default -> serverLogger.info("Unknown command line argument: {}", args[i]);
      }
    }
    // Print configuration parameters
    serverLogger.info("Configuration:\n* Port: {}\n* Heartbeat: {}\n* Idle game deletion timeout: {} s",
        serverArgs.port,
        serverArgs.heartbeat,
        serverArgs.deleteTimeout
    );

    ServerApp serverApp = new ServerApp(serverArgs);
    serverApp.run();
  }
}
