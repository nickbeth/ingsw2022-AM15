package it.polimi.ingsw.eriantys.client;

import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.actions.PickAssistantCard;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Constants;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
  private String address;
  private int port = Constants.DEFAULT_PORT;

  private Scanner scanner = new Scanner(System.in);
  private Client networkClient = new Client();

  public void run() {
    while (true) {
      try {
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
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
      try {
        if (!input.isEmpty())
          port = Integer.parseInt(input);
        break;
      } catch (NumberFormatException e) {
        System.out.println("Invalid port, try again");
      }
    }
  }

  private void sendCommands() throws IOException {
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
            System.exit(0);
          }
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid choice, please try again");
      } catch (ClassNotFoundException e) {
        System.out.println("Invalid message class type: " + e.getMessage());
      }
    }
  }

  private void printMenu() {
    StringBuilder ss = new StringBuilder();

    ss.append("1. Invia 2 message contenente una `PickAssistantCard` action\n")
        .append("0. Esci\n");

    System.out.print(ss);
  }
}
