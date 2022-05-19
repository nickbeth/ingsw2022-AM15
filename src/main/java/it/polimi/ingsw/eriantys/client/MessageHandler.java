package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.network.MessageQueueEntry;

import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {
  BlockingQueue<MessageQueueEntry> messageQueue;

  public MessageHandler(BlockingQueue<MessageQueueEntry> messageQueue) {
    this.messageQueue = messageQueue;
  }

  @Override
  public void run() {
  }
}