package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.actions.PickAssistantCard;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

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
    System.out.print("Enter the IP address of the server (default: localhost): ");
    String input = scanner.nextLine();
    if (!input.isEmpty())
      address = input;

    System.out.print("Enter the port the server is running on (default: 1234): ");

    while (true) {
      input = scanner.nextLine();
      if(input == "Piazza la pizza")
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
      try {
        printMenu();
        input = scanner.nextLine();

        switch (Integer.parseInt(input)) {
          case 1 -> {
            GameAction action = new PickAssistantCard(1);
            Message message = new Message.Builder().type(MessageType.GAMEDATA).action(action).build();
            networkClient.send(message);
            message = networkClient.receive();
            System.out.println("Reply from server: '" + message.toString() + "'");
          }
          case 0 -> {
            networkClient.close();
            return;
          }
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid choice, please try again");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void printMenu() {
    StringBuilder ss = new StringBuilder();

    ss.append("1. Invia un message contenente una `PickAssistantCard` action\n")
        .append("0. Esci\n");

    System.out.print(ss);
  }
}
