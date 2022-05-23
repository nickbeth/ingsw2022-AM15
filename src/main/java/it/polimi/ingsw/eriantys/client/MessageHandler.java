package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.ObservableActionInvoker;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;
import org.tinylog.Logger;

import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {
  Controller controller;
  BlockingQueue<MessageQueueEntry> messageQueue;

  public MessageHandler(Controller controller, BlockingQueue<MessageQueueEntry> messageQueue) {
    this.controller = controller;
    this.messageQueue = messageQueue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        MessageQueueEntry entry = messageQueue.take();
        Logger.trace("Handling response: {}", entry);
        handleMessage(entry);
      } catch (InterruptedException e) {
        // We should never be interrupted
        throw new AssertionError(e);
      }
    }
  }

  private void handleMessage(MessageQueueEntry entry) {
    Client client = entry.client();
    Message message = entry.message();

    if (message.type() == null) {
      Logger.warn("Received a message with an invalid message type: {}", message);
      return;
    }

    switch (message.type()) {
      case PING -> handlePing(client, message);

      case GAMEINFO -> handleGameInfo(client, message);
      case GAMEDATA -> handleGameData(client, message);

      case ERROR -> handleError(client, message);

      case INTERNAL_SOCKET_ERROR -> handleSocketError(client, message);
    }
  }

  private void handlePing(Client client, Message message) {
    client.send(new Message.Builder(MessageType.PONG)
            .nickname(controller.getNickname())
            .gameCode(controller.getGameCode())
            .build());
  }

  private void handleGameInfo(Client client, Message message) {
    controller.setGameInfo(message.gameInfo());
  }

  private void handleGameData(Client client, Message message) {
    ObservableActionInvoker actionInvoker = controller.getActionInvoker();
    actionInvoker.executeAction(message.gameAction());
  }

  private void handleError(Client client, Message message) {
    controller.showError(message.error());
  }

  private void handleSocketError(Client client, Message message) {
    // Check that this message was created internally and is not coming from the network
    if (!message.nickname().equals(Client.SOCKET_ERROR_HASH))
      return;

    String errorMessage = "Lost connection to the server";
    if (message.error() != null)
      errorMessage += ": " + message.error();

    controller.showError(errorMessage);
    client.close();
  }
}