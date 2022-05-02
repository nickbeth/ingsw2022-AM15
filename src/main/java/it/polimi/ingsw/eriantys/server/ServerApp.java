package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Server;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class ServerApp {
  private final int port;
  private Server networkServer = new Server();

  private ArrayList<Client> clients = new ArrayList<>();

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
        String recv = client.receive();
        Logger.info("Received: " + recv);
        client.send("Server says thank you!");
      }
    } catch (IOException e) {
      Logger.error(e.getMessage());
    }
  }
}
