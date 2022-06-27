package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.Server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.eriantys.loggers.Loggers.serverLogger;


public class ServerApp {
  private final Server networkServer;
  private final GameServer gameServer;
  private Thread appThread;

  ServerApp(ServerArgs args) {
    // Create a shared queue between the network server and the game server
    // Received messages will be added to this queue
    // The game server will poll the queue for messages to process
    BlockingQueue<MessageQueueEntry> messageQueue = new LinkedBlockingQueue<>();
    this.networkServer = new Server(args.port, messageQueue);
    this.gameServer = new GameServer(args.heartbeat, args.deleteTimeout, messageQueue);
  }

  public void run() {
    serverLogger.info("Server booting");
    try {
      // Initialize the network server and launch the accepting thread
      networkServer.init();
      Thread acceptThread = new Thread(networkServer, "accept");
      acceptThread.setDaemon(true);
      acceptThread.start();

      // Run the game server loop in this thread
      appThread = Thread.currentThread();
      gameServer.run();
    } catch (IOException e) {
      serverLogger.error("Server failed to initialize", e);
    }
    serverLogger.info("Server stopped");
  }

  public void exit() {
    gameServer.exit();
    if (appThread != null) {
      appThread.interrupt();
    }
  }

  public void exitAndJoin() {
    try {
      exit();
      appThread.join();
    } catch (InterruptedException ignored) {
    }
  }
}
