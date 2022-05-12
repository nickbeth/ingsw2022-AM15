package it.polimi.ingsw.eriantys.network;

import java.util.concurrent.BlockingQueue;

public class GameServer implements Runnable {
  private final BlockingQueue<MessageQueueEntry> messageQueue;

  public GameServer(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
  }

  /**
   * Runs the game server loop.
   * This method is supposed to be run on its own thread.
   */
  @Override
  public void run() {
    while (true) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        Client client = entry.client();
        Message message = entry.message();
        System.out.println(entry);
        client.send(message);
      } catch (InterruptedException e) {
        // We should never be interrupted
        throw new AssertionError(e);
      }
    }
  }
}
