package it.polimi.ingsw.eriantys.network;

import org.tinylog.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 * A wrapper around java.io.Socket for sending and receiving Message objects.
 */
public class Client implements Runnable {
  public static final int DEFAULT_PORT = Server.DEFAULT_PORT;
  
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  
  private final BlockingQueue<MessageQueueEntry> messageQueue;
  
  /**
   * Default constructor, creates and empty client
   */
  public Client(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
  }
  
  /**
   * Creates a new client from the given socket
   *
   * @param socket The client socket
   */
  Client(Socket socket, BlockingQueue<MessageQueueEntry> messageQueue) throws IOException {
    this.socket = socket;
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
    this.messageQueue = messageQueue;
  }
  
  /**
   * Connects to a remote server by creating a new socket
   *
   * @param address The server address
   * @param port    The server port
   */
  public void connect(String address, int port) throws IOException {
    try {
      socket = new Socket(address, port);
    } catch (UnknownHostException e) {
      System.out.println("Impossible to connect to the server. Check ip and port.");
    }
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
    Logger.info("Connected to: {}", socket.getRemoteSocketAddress());
  }
  
  /**
   * Sends a message.
   *
   * @param msg The message to send
   */
  public void send(Message msg) {
    try {
      out.writeObject(msg);
      out.flush();
    } catch (IOException e) {
      Logger.error("Message not sent " + e.getMessage());
    }
  }
  
  /**
   * Receives a message. This method blocks until a message is received.
   *
   * @return The received message
   */
  public Message receive() throws IOException, ClassNotFoundException {
    return (Message) in.readObject();
  }
  
  /**
   * Closes this socket.
   */
  public void close() throws IOException {
    socket.close();
  }
  
  /**
   * Runs the client listening loop. Receive messages and adds them to the message queue.
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    Logger.debug("Starting thread '{}'", Thread.currentThread().getName());
    try {
      //noinspection InfiniteLoopStatement
      while (true) {
        try {
          messageQueue.add(new MessageQueueEntry(this, receive()));
        } catch (ClassNotFoundException e) {
          Logger.error("Received invalid message: {}", e);
        }
      }
    } catch (EOFException e) {
      // Client disconnected while we were waiting on receive
      Logger.debug("Client '{}' disconnected", this);
    } catch (SocketException e) {
    
    } catch (IOException e) {
      Logger.error("An error occurred on socket '{}': {}", this, e);
    }
    Logger.debug("Stopping thread '{}'", Thread.currentThread().getName());
  }
  
  /**
   * Prints this socket in the following format: [hostname]/[host address]:[port]
   */
  @Override
  public String toString() {
    return socket.getRemoteSocketAddress().toString();
  }
}
