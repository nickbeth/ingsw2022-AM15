package it.polimi.ingsw.eriantys.network;

import java.util.concurrent.BlockingQueue;

public class GameServer implements Runnable {
  private final BlockingQueue<Message> messageQueue;

  public GameServer(BlockingQueue<Message> messageQueue) {
    this.messageQueue = messageQueue;
  }

  /**
   * Runs the game server loop.
   */
  @Override
  public void run() {
    while (true) {
      try {
        Message message = messageQueue.take();
        System.out.println(message);
      } catch (InterruptedException e) {
        // We should never be interrupted
        throw new AssertionError(e);
      }
    }
  }
}
