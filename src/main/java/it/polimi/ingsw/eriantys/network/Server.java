package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static final int DEFAULT_PORT = 1234;

  private ServerSocket serverSocket;
  private PrintWriter out;
  private BufferedReader in;

  /**
   * Starts the server in listening mode.
   *
   * @param port The server port
   */
  public void start(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    Logger.debug("Server socket up on {}:{}", serverSocket.getInetAddress(), serverSocket.getLocalPort());
  }

  public Client accept() throws IOException {
    Socket clientSocket = serverSocket.accept();
    Logger.debug("Accepting incoming client: {}", clientSocket.getRemoteSocketAddress());
    return new Client(clientSocket);
  }
}

