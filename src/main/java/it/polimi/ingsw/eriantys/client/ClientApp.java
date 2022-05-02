package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.network.Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
  private String address;
  private int port = Client.DEFAULT_PORT;

  private Scanner scanner = new Scanner(System.in);
  private Client networkClient = new Client();

  public void run() {
    while (true) {
      try {
        readServerAddress();
        networkClient.connect(address, port);
        break;
      } catch (IOException e) {
        System.out.println("Failed to connect to the server: '" + e.getMessage() + "'");
      }
    }
    sendCommands();
  }

  /**
   * Asks the user for server's address and port
   */
  private void readServerAddress() {
    System.out.println("Enter the IP address of the server (default: localhost):");
    String input = scanner.nextLine();
    if (!input.isEmpty())
      address = input;

    System.out.println("Enter the port the server is running on (default: 1234):");

    while (true) {
      input = scanner.nextLine();
      try {
        if (!input.isEmpty())
          port = Integer.parseInt(input);
        break;
      } catch (NumberFormatException e) {
        System.out.println("Invalid port, try again");
      }
    }
  }

  private void sendCommands() {
    String input;
    while (true) {
      System.out.println("Enter the command to send to the server: ");
      input = scanner.nextLine();
      try {
        networkClient.send(input);
        System.out.println(networkClient.receive());
      } catch (IOException ignored) {
      }
    }
  }
}
