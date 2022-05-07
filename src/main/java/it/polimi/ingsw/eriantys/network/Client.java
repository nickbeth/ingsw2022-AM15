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

  /**
   * Sends a message.
   * @param msg The message to send
   */
  public void send(Message msg) throws IOException {
    out.writeObject(msg);
    out.flush();
  }

  /**
   * Receives a message. This method blocks until a message is received.
   * @return The received message
   */
  public Message receive() throws IOException, ClassNotFoundException {
    try {
      return (Message) in.readObject();
    } catch (ClassNotFoundException e) {
      Logger.error("Invalid message class type: {}", e.getMessage());
      throw e;
    }
  }

  /**
   * Closes this socket.
   */
  public void close() throws IOException {
    socket.close();
  }

  /**
   * Prints this socket in the following format: [hostname]/[host address]:[port]
   */
  @Override
  public String toString() {
    return socket.getRemoteSocketAddress().toString();
  }
}
