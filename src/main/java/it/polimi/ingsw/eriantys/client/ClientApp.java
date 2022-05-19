package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientApp {
  private final boolean isGui;

  private final Client networkClient;
  private final MessageHandler messageHandler;

  private final Controller controller;

  public ClientApp(boolean isGui) {
    this.isGui = isGui;
    BlockingQueue<MessageQueueEntry> messageQueue = new LinkedBlockingQueue<>();
    this.networkClient = new Client(messageQueue);
    this.messageHandler = new MessageHandler(messageQueue);
    this.controller = Controller.create(isGui, networkClient, messageHandler);
  }

  public void run() {
    controller.run();
  }
}
