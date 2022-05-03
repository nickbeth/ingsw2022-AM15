package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.*;
import java.net.Socket;

/**
 * A wrapper around java.io.Socket for sending and receiving Message objects.
 */
public class Client {
  public static final int DEFAULT_PORT = Server.DEFAULT_PORT;

  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

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
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
  }

  /**
   * Connects to a remote server by creating a new socket
   *
   * @param address The server address
   * @param port    The server port
   */
  public void connect(String address, int port) throws IOException {
    socket = new Socket(address, port);
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
    Logger.info("Connected to: {}", socket.getRemoteSocketAddress());
  }

  public void send(Message msg) throws IOException {
    out.writeObject(msg);
    out.flush();
  }

  public Message receive() throws IOException {
    Message recv = null;
    try {
      recv = (Message) in.readObject();
    } catch (ClassNotFoundException e) {
      Logger.error("Invalid message type: {}", e.getMessage());
    }
    return recv;
  }

  public void close() throws IOException {
    socket.close();
  }

  public String toString() {
    return socket.getRemoteSocketAddress().toString();
  }
}
