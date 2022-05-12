package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Server implements Runnable {
  public static final int DEFAULT_PORT = 1234;

  /**
   * The number of errors that can occur before the server is restarted.
   */
  private static final int ACCEPT_RESTART_THRESHOLD = 5;

  int port;
  private ServerSocket serverSocket;
  private final BlockingQueue<Message> messageQueue;

  /**
   * Creates a new server.
   *
   * @param port         The server port
   * @param messageQueue The shared queue where received messages will be added
   */
  public Server(int port, BlockingQueue<Message> messageQueue) {
    this.port = port;
    this.messageQueue = messageQueue;
  }

  /**
   * Initializes the server socket.
   */
  public void start() throws IOException {
    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException ignored) {
      }
    }
    serverSocket = new ServerSocket(port);
    Logger.info("Server socket up on {}", serverSocket.getLocalSocketAddress());
  }

  /**
   * Accepts a new client connection.
   *
   * @return The accepted client
   */
  public Client accept() throws IOException {
    Logger.debug("Waiting for incoming connections");
    Socket clientSocket = serverSocket.accept();
    Logger.debug("Accepted incoming client: {}", clientSocket.getRemoteSocketAddress());
    return new Client(clientSocket);
  }

  /**
   * Runs the server accept loop.
   */
  @Override
  public void run() {
    Logger.debug("Starting thread '{}'", Thread.currentThread().getName());
    int threadSeqNumber = 1;
    int errorCount = 0;
    while (true) {
      try {
        Client newClient = accept();
        // Reset the error count after every successful accept
        errorCount = 0;
        Thread t = new Thread(() -> clientListenerThread(newClient));
        t.setName("sock-" + threadSeqNumber++);
        t.start();
      } catch (IOException e) {
        Logger.error("An error occurred while accepting: {}", e);
        if (++errorCount > ACCEPT_RESTART_THRESHOLD) {
          try {
            start();
            Logger.warn("The server encountered a fatal error and was restarted");
          } catch (IOException ex) {
            Logger.error("The server encountered a fatal error and could not be restarted: {}", ex);
            break;
          }
        }
      }
    }
    Logger.debug("Stopping thread '{}'", Thread.currentThread().getName());
  }

  /**
   * The thread that will listen to the client and add received messages to the queue.
   *
   * @param client The client to listen to
   */
  private void clientListenerThread(Client client) {
    Logger.debug("Starting thread '{}'", Thread.currentThread().getName());
    try {
      while (true) {
        try {
          messageQueue.add(client.receive());
        } catch (ClassNotFoundException e) {
          Logger.error("Received invalid message: {}", e.getMessage());
        }
      }
    } catch (IOException e) {
      Logger.error("An error occurred on client '{}': {}", client, e.getMessage());
    }
    Logger.debug("Stopping thread '{}'", Thread.currentThread().getName());
  }
}
