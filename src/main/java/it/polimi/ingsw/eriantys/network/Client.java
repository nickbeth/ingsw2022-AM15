package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * A wrapper around java.io.Socket for sending and receiving Message objects.
 */
public class Client {
  private Socket socket;
  private OutputStream out;
  private InputStream in;

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
    out = socket.getOutputStream();
    in = socket.getInputStream();
  }

  /**
   * Connects to a remote server by creating a new socket
   *
   * @param address The server address
   * @param port    The server port
   */
  public void connect(String address, int port) throws IOException {
    socket = new Socket(address, port);
    out = socket.getOutputStream();
    in = socket.getInputStream();
    Logger.info("Connected to: {}", socket.getRemoteSocketAddress());
  }

  /**
   * Sends a message.
   *
   * @param msg The message to send
   */
  public void send(Message msg) throws IOException {
    ByteBuffer serialized = SerializationHelper.serialize(msg);
    out.write(serialized.array(), 0, serialized.limit());
    out.flush();
  }

  /**
   * Receives a message. This method blocks until a message is received.
   *
   * @return The received message
   */
  public Message receive() throws IOException, ClassNotFoundException {
    try {
      ObjectInputStream ois = new ObjectInputStream(in);
      return (Message) ois.readObject();
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
