package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.Server;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerApp {
  private final int port;
  private final Server networkServer;
  private final GameServer gameServer;

  ServerApp(int port) {
    this.port = port;
    // Create a shared queue between the network server and the game server
    // Messages sent by clients will be added to this queue
    // The game server will poll the queue for messages to process
    BlockingQueue<MessageQueueEntry> messageQueue = new LinkedBlockingQueue<>();
    this.networkServer = new Server(port, messageQueue);
    this.gameServer = new GameServer(messageQueue);
  }

  public void run() {
    try {
      // Initialize then network server and launch the accepting thread
      networkServer.init();
      new Thread(networkServer, "accept").start();

      // Run the game server loop in this thread
      gameServer.run();
    } catch (IOException e) {
      Logger.error("Server failed to initialize: {}", e);
    }
  }
}
