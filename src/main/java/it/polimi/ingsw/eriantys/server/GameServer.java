package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.network.ClientChannel;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageType;

import java.io.IOException;
import java.util.HashMap;

class GameServer {
  HashMap<String, GameState> activeGames = new HashMap<>();

  public void handleMessage(ClientChannel client, Message message) {
    System.out.println("Received message: '" + message.toString() + "' from: " + client);
    if (message.type() == MessageType.GAMEDATA) {
      System.out.println();
      System.out.println("Placeholder: should execute '" + message.gameAction() + "' on the server game state here");
      try {
        client.write(message);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
