package it.polimi.ingsw.eriantys.network;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

/**
 * A wrapper around {@link Socket java.net.Socket} for sending and receiving Message objects.
 *
 * <p> This class supports the <i>attachment</i> of a single arbitrary object.
 * An object can be attached via the {@link #attach attach} method and then later retrieved via
 * the {@link #attachment() attachment} method.  </p>
 */
public class Client implements Runnable {
  public static final int DEFAULT_PORT = Server.DEFAULT_PORT;
  public static final String SOCKET_ERROR_HASH = "7a91c9d6";

  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  private final BlockingQueue<MessageQueueEntry> messageQueue;
  private final AtomicBoolean closed = new AtomicBoolean(false);

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
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.in = new ObjectInputStream(socket.getInputStream());
    this.messageQueue = messageQueue;
    this.attachment = null;
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
    closed.set(false);
    clientLogger.info("Connected to: {}", socket.getRemoteSocketAddress());
  }

  /**
   * Sends a message.
   *
   * @param msg The message to send
   */
  public synchronized void send(Message msg) {
    if (!closed.get()) {
      try {
        out.writeObject(msg);
        out.reset();
        out.flush();
      } catch (IOException e) {
        clientLogger.error("Couldn't send message to '{}': {}: {}", this, e.getClass().getName(), e.getMessage());
      }
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
  public void close() {
    closed.set(true);
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException ignored) {
      }
    }
  }

  /**
   * Returns the closed socket state. After a {@link Client} has been closed,
   * every operation on it will result in undefined behaviour and should be avoided,
   * until {@link #connect(String, int)} is called on it again.
   *
   * @return {@code true} if the socket was closed, {@code false} otherwise
   */
  public boolean isClosed() {
    return closed.get();
  }

  /**
   * Runs the client listening loop.
   * Receives messages from the socket and adds them to the message queue.
   * <p>
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    clientLogger.debug("Starting thread '{}'", Thread.currentThread().getName());
    try {
      while (!closed.get()) {
        try {
          messageQueue.add(new MessageQueueEntry(this, receive()));
        } catch (ClassNotFoundException e) {
          clientLogger.error("Received invalid message: ", e);
        }
      }
    } catch (IOException e) {
      clientLogger.error("An error occurred on socket '{}': {}", this, e.getMessage());
      // We need a way to notify message handlers of socket errors
      // We submit a special message to the queue so that it can be handled
      messageQueue.add(new MessageQueueEntry(this, new Message.Builder()
          .type(MessageType.INTERNAL_SOCKET_ERROR).nickname(SOCKET_ERROR_HASH).error(e.getMessage()).build()));
    }
    clientLogger.debug("Stopping thread '{}'", Thread.currentThread().getName());
  }

  /**
   * A VarHandle of the attachment object, used for fast atomic access.
   *
   * @implNote Requires Java9+
   */
  private static final VarHandle ATTACHMENT;

  static {
    try {
      MethodHandles.Lookup l = MethodHandles.lookup();
      ATTACHMENT = l.findVarHandle(Client.class, "attachment", Object.class);
    } catch (Exception e) {
      throw new InternalError(e);
    }
  }

  private volatile Object attachment;

  /**
   * Attaches the given object to this key.
   *
   * <p> An attached object may later be retrieved via the {@link #attachment()
   * attachment} method.  Only one object may be attached at a time; invoking
   * this method causes any previous attachment to be discarded.  The current
   * attachment may be discarded by attaching {@code null}.  </p>
   *
   * @param ob The object to be attached; may be {@code null}
   * @return The previously-attached object, if any,
   * otherwise {@code null}
   */
  public final Object attach(Object ob) {
    return ATTACHMENT.getAndSet(this, ob);
  }

  /**
   * Retrieves the current attachment.
   *
   * @return The object currently attached to this key,
   * or {@code null} if there is no attachment
   */
  public final Object attachment() {
    return attachment;
  }

  /**
   * Prints this socket in the following format: [hostname]/[host address]:[port]
   */
  @Override
  public String toString() {
    if (socket == null)
      return "uninitialized";
    return socket.getRemoteSocketAddress().toString();
  }
}
