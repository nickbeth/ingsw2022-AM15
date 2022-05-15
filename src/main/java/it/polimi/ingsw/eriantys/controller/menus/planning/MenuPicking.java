package it.polimi.ingsw.eriantys.controller.menus.planning;

import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.controller.menus.action.MenuActions;
import it.polimi.ingsw.eriantys.model.GameState;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPicking extends Menu {

  public MenuPicking(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      System.out.println("A - Choose assistant card");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        case "A", "a" -> {
          (new AssistantCardsView(game.getPlayer(playerNickname))).draw(System.out);
          try {
            System.out.print("Choose card index:");
            controller.sendPickAssistantCard(s.nextInt());
            done = true;
          } catch (InputMismatchException e) {
            System.out.println("Please insert a number");
          }
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return (new MenuActions(game, playerNickname, controller));
  }
}
