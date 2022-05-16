package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuEffect extends Menu {

  public MenuEffect(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      System.out.println("E - Choose character card");
      System.out.println("R - Activate character card effect");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        // Choose a character card from those in playing field
        case "E", "e" -> {
          if (!game.getTurnPhase().equals(TurnPhase.EFFECT))
            break;
          int ccIndex = -1;
          try {
            System.out.println("Playable character cards: ");
            //todo stampare a schermo le character card

            System.out.println("Choose a character card: ");
            ccIndex = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Input must be a number");
          }
          if (!controller.sendChooseCharacterCard(1)) {
            System.out.println("Invalid input parameters");
          }
        }
        //todo activate CC effect
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
