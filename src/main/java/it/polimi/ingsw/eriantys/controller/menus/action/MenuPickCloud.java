package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.AssistantCardsView;
import it.polimi.ingsw.eriantys.cli.views.CloudsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPickCloud extends Menu {

  public MenuPickCloud(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      System.out.println("Q - Pick cloud");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        case "Q", "q" -> {
          System.out.println("Choose cloud index: ");
          (new CloudsView(game.getPlayingField().getClouds())).draw(System.out);
          if (controller.sendPickCloud(0))
            done = true;
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
