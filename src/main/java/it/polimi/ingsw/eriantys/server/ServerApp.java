package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.ClientChannel;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.SerializationHelper;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerApp {
  private static final int MAX_BUFFER_SIZE = 16768;

  private final int port;
  private Selector selector;
  private final ThreadPoolExecutor executor;

  private final GameServer gameServer;

  ServerApp(int port) {
    this.port = port;
    this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    this.gameServer = new GameServer();
  }

  public void run() {
    try {
      init();
      loop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * The main loop of the server. It uses a selector to wait for
   * new connections and messages and dispatches them to a thread pool.
   */
  private void loop() {
    while (true) {
      try {
        selector.select();
        Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
          SelectionKey key = selectedKeys.next();
          selectedKeys.remove();

          if (!key.isValid())
            continue;

          if (key.isAcceptable())
            accept(key);
          else if (key.isReadable())
            read(key);
        }
      } catch (IOException e) {
        Logger.error("An error occured while selecting: {}", e.getMessage());
      }
    }
  }

  /**
   * Accepts a new connection and adds it to the selector for reading operations.
   */
  private void accept(SelectionKey key) {
    try {
      ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

      SocketChannel socketChannel = serverSocketChannel.accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, SelectionKey.OP_READ);
      Logger.debug("Accepted connection from {}", socketChannel.getRemoteAddress());
    } catch (IOException e) {
      Logger.error("An error occurred while accepting a connection: {}", e.getMessage());
    }
  }

  /**
   * Reads a message from a client and dispatches it to the game server.
   */
  private void read(SelectionKey key) {
    try {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      ByteBuffer readBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
      socketChannel.read(readBuffer);
      readBuffer.flip();
      executor.submit(() -> {
        try {
          gameServer.handleMessage(new ClientChannel(key), SerializationHelper.deserialize(readBuffer));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (IOException e) {
      Logger.error("An error occurred while reading from the client: {}", e.getMessage());
    }
  }

  /**
   * Starts up the server on the given port.
   */
  private void init() throws IOException {
    // Initialize the server socket channel
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);
    var hostAddress = new InetSocketAddress((InetAddress) null, port);
    serverChannel.bind(hostAddress);
    Logger.debug("Server socket channel created on {}", hostAddress);

    // Initialize the selector
    selector = Selector.open();
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
  }
}
