package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuMoving extends Menu {

  public MenuMoving(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      System.out.println("T - Move mother nature");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        // Move mother nature a certain amount
        case "T", "t" -> {
          // Check of the Turn phase
          if (!game.getTurnPhase().equals(TurnPhase.MOVING))
            break;

          // Takes amount input and send the action
          int amount = -1;
          try {
            // Shows islands
            System.out.println("Playing Field: ");
            (new IslandsView(game.getPlayingField().getIslands(),
                    game.getPlayingField().getMotherNaturePosition())).draw(System.out);
            System.out.println("Insert the amount of mother nature movements: ");
            amount = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Input must be a number");
          }
          //sends action
          if (!controller.sendMoveMotherNature(amount)) {
            System.out.println("Invalid input parameters");
          }
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  //todo pensare bene al next menu
  @Override
  public Menu nextMenu() {
    return (new MenuPlacing(game, playerNickname, controller));
  }

}
