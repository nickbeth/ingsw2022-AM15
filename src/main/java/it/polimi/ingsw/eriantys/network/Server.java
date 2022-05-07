package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static final int DEFAULT_PORT = 1234;

  private ServerSocket serverSocket;

  /**
   * Starts the server in listening mode.
   *
   * @param port The server port
   */
  public void start(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    Logger.debug("Server socket created on {}", serverSocket.getLocalSocketAddress());
  }

  /**
   * Accepts a new client connection.
   * @return The accepted client
   */
  public Client accept() throws IOException {
    Logger.debug("Server waiting for incoming connections");
    Socket clientSocket = serverSocket.accept();
    Logger.debug("Accepting incoming client: {}", clientSocket.getRemoteSocketAddress());
    return new Client(clientSocket);
  }
}

