package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuActions extends Menu {
  private int studentMoved = 0;

  public MenuActions(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    int studentsLeft = game.getRuleBook().playableStudentCount - studentMoved;
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      System.out.println(
              MessageFormat.format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
      System.out.println(
              MessageFormat.format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
      System.out.println("E - Choose character card");
      System.out.println("R - Activate character card effect");
      System.out.println("T - Move mother nature");
      System.out.println("Y - Pick a cloud");
    }
  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    Scanner s = new Scanner(System.in);
    boolean done = false;

    do {
      showOptions();
      switch (s.nextLine()) {
        // Move Students from entrance to island
        case "Q", "q" -> {
          // Check of the Turn phase
          if (!game.getTurnPhase().equals(TurnPhase.PLACING)) {
            System.out.println("Action not available in this phase.");
            break;
          }
          paramBuilder.flushStudentToMove();

          // Takes the color
          (new MenuColor()).makeChoice(paramBuilder);
          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Takes island input and send the action
          int islandIndex = -1;
          try {
            // Show islands
            System.out.println("Choose island: ");
            (new IslandsView(game.getPlayingField().getIslands())).draw(System.out);
            islandIndex = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Number required");
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
          }
          // Send actions
          if (!controller.sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
            System.out.println("Invalid input parameters.");
          }
        }
        // Move Students from entrance to dining
        case "W", "w" -> {
          // Check of the Turn phase
          if (!game.getTurnPhase().equals(TurnPhase.PLACING)) {
            System.out.println("Action not available in this phase.");
            break;
          }
          paramBuilder.flushStudentToMove();

          // Takes the color
          (new MenuColor()).makeChoice(paramBuilder);
          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Send actions
          if (!controller.sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
            System.out.println("Invalid input parameters.");
          }
        }
        // Pick a cloud
        case "Y", "y" -> {
          if (controller.sendPickCloud(0))
            done = true;
        }
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return null;
  }
}
