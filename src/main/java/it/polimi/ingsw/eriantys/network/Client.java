package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
  public static final int DEFAULT_PORT = Server.DEFAULT_PORT;

  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  /**
   * Default constructor, creates and empty client
   */
  public Client() {
  }

  /**
   * Creates a new client from the given socket
   *
   * @param socket The client socket
   */
  Client(Socket socket) throws IOException {
    this.socket = socket;
    out = new PrintWriter(socket.getOutputStream());
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  /**
   * Connects to a remote server by creating a new socket
   *
   * @param address The server address
   * @param port    The server port
   */
  public void connect(String address, int port) throws IOException {
    socket = new Socket(address, port);
    out = new PrintWriter(socket.getOutputStream());
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    Logger.info("Connected to: {}", socket.getRemoteSocketAddress());
  }

  public <T> void send(T msg) throws IOException {
    out.println(msg.toString());
    out.flush();
  }

  public String receive() throws IOException {
    return in.readLine();
  }

  public String toString() {
    return socket.getRemoteSocketAddress().toString();
  }
}
