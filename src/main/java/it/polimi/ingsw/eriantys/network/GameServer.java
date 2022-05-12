package it.polimi.ingsw.eriantys.network;

import java.util.concurrent.BlockingQueue;

public class GameServer implements Runnable {
  private final BlockingQueue<MessageQueueEntry> messageQueue;

  public GameServer(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
  }

  /**
   * Runs the game server loop.
   */
  @Override
  public void run() {
    while (true) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        Client client = entry.client();
        Message message = entry.message();
        client.send(message);
        System.out.println(entry);
      } catch (InterruptedException e) {
        // We should never be interrupted
        throw new AssertionError(e);
      }
    }
  }
}
