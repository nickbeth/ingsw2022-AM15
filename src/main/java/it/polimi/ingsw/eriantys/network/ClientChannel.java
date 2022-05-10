package it.polimi.ingsw.eriantys.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * A wrapper around a selected {@link SocketChannel} which is able to send and receive {@link Message} objects.
 */
public class ClientChannel {
  private static final int MAX_BUFFER_SIZE = 16768;

  private final SelectionKey key;
  private final SocketChannel socketChannel;

  public ClientChannel(SelectionKey key) {
    this.key = key;
    this.socketChannel = (SocketChannel) key.channel();
  }

  public void write(Message message) throws IOException {
    ByteBuffer writeBuffer = SerializationHelper.serialize(message);

    while (writeBuffer.remaining() > 0) {
      socketChannel.write(writeBuffer);
    }
  }

  public void close() throws IOException {
    key.cancel();
    socketChannel.close();
  }
}
