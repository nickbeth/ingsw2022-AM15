package it.polimi.ingsw.eriantys.cli.menus.planning;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.action.MenuPlacing;
import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;
import it.polimi.ingsw.eriantys.controller.CliController;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPickAssistantCard extends Menu {
  public MenuPickAssistantCard(CliController controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions();
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      System.out.println("A - Choose assistant card");
    }
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;

    do {
      showOptions(out);
      switch (in.nextLine()) {
        case "A", "a" -> {
          (new AssistantCardsView(controller.getGameState().getPlayer(controller.getNickname()))).draw(System.out);
          int index = -1;
          try {
            System.out.print("Choose card index:");
            done = true;
          } catch (InputMismatchException e) {
            System.out.println("Please insert a number");
          }
          if (!controller.sendPickAssistantCard(index)) {
            System.out.println("Invalid input parameters.");
          }
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuPlacing(controller);
  }
}
