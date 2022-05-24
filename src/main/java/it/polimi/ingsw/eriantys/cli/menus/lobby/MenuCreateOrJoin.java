package it.polimi.ingsw.eriantys.cli.menus.lobby;

import it.polimi.ingsw.eriantys.cli.menus.Menu;
import it.polimi.ingsw.eriantys.controller.CliController;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventEnum.GAMEINFO_EVENT;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.EXPERT;
import static it.polimi.ingsw.eriantys.model.enums.GameMode.NORMAL;

/**
 * Asks the user for server's address and port
 */
public class MenuCreateOrJoin extends Menu {
  private boolean gameCodeExists;
  
  public MenuCreateOrJoin(CliController controller) {
    this.controller = controller;
    controller.addListener(this, GAMEINFO_EVENT.tag);
  }
  
  @Override
  protected void showOptions(PrintStream out) {
    out.println("1 - Create a new game");
    out.println("2 - Join an existing game");
  }
  
  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;
    
    do {
      greenLight = false;
      showOptions(out);
      switch (in.nextLine()) {
        // Create a new game
        case "1" -> {
          gameCodeExists = false;
          showCreateOptions(in, out);
          waitForGreenLight();
          done = false;
          if (gameCodeExists) {
            out.println("Gamecode already exists");
            done = true;
          }
        }
        // Join to a game
        case "2" -> {
          gameCodeExists = true;
          showJoinOptions(in, out);
          waitForGreenLight();
          done = false;
          if (!gameCodeExists) {
            out.println("Gamecode does not exist");
            done = true;
          }
        }
        default -> out.println("Invalid choice");
      }
    }
    while (!done);
  }
  
  @Override
  public Menu next() {
    return new MenuGameInfo(controller);
  }
  
  private void showCreateOptions(Scanner in, PrintStream out) {
    boolean valid;
    GameMode mode = null;
    int playersCount;
    
    // Get GameMode
    do {
      valid = false;
      out.print("Enter the game mode: " +
              "\n1 - " + NORMAL +
              "\n2 - " + EXPERT);
      short choice = in.nextShort();
      valid = choice == 1 || choice == 2;
      mode = choice == 1 ? NORMAL : EXPERT;
      if (!valid) out.println("Enter a valid choice.");
    } while (valid);
    // Get playersCount
    do {
      out.print("Enter the number of players: ");
      playersCount = in.nextShort();
      valid = playersCount >= 2 && playersCount <= 4;
      if (!valid) out.println("Enter a valid choice.");
    } while (valid);
    
    controller.sender().sendCreateGame(playersCount, mode);
  }
  
  private void showJoinOptions(Scanner in, PrintStream out) {
    // TODO: code entered valid?
    out.print("Enter the game code: ");
    controller.sender().sendJoinGame(in.nextLine());
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    gameCodeExists = evt.getPropertyName().equals(GAMEINFO_EVENT.tag);
  }
}
