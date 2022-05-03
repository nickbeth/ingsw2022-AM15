package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.*;
import org.tinylog.Logger;

import java.io.IOException;

public class ServerApp {
  private final int port;
  private Server networkServer = new Server();

  ServerApp(int port) {
    this.port = port;
  }

  public void run() {
    try {
      networkServer.start(port);
      runListener();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void runListener() {
    while (true) {
      try {
        Client newClient = networkServer.accept();
        new Thread(() -> handleClient(newClient)).start();
      } catch (IOException e) {
        Logger.error(e.getMessage());
      }
    }
  }

  private void handleClient(Client client) {
    try {
      while (true) {
        Message recv = client.receive();
        System.out.println("Received message: '" + recv.toString() + "' from: " + client);
        if (recv.type() == MessageType.GAMEDATA) {
          System.out.println();
          System.out.println("Placeholder: should execute '" + recv.gameAction() + "' on the server game state here");
          client.send(recv);
        }
      }
    } catch (IOException e) {
      Logger.error(e.getMessage());
    }
  }
}
