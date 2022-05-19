package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.CharacterCardView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPlacing extends Menu {
  private int studentMoved = 0;

  public MenuPlacing(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    showViewOptions();
    int studentsLeft = controller.getGameState().getRuleBook().playableStudentCount - studentMoved;
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      if (controller.getGameState().getTurnPhase() == TurnPhase.PLACING) {
        System.out.println(
                MessageFormat.format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
        System.out.println(
                MessageFormat.format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
        System.out.println("E - Play a character card");
      }
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
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          paramBuilder.flushStudentToMove();

          // Shows entrance
          (new DashboardView(controller.getGameState().getRuleBook(), controller.getGameState().getCurrentPlayer().getDashboard(),
                  controller.getGameState().getPlayingField().getProfessorHolder())).draw(System.out);

          // Takes the color
          (new MenuStudentColor()).makeChoice(paramBuilder);

          // Ask for amount
          int amount;
          try {
            System.out.print("Amount: ");
            amount = s.nextInt();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
          } catch (InputMismatchException e) {
            System.out.println("Insert a number");
          }

          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Takes island input and send the action
          int islandIndex = -1;
          try {
            // Shows islands
            System.out.println("Choose an island: ");
            (new IslandsView(controller.getGameState().getPlayingField().getIslands(),
                    controller.getGameState().getPlayingField().getMotherNaturePosition())).draw(System.out);
            islandIndex = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Number required");
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
          }
          // Send actions
          if (!controller.sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
            System.out.println("Invalid input parameters");
          }
        }
        // Move Students from entrance to dining
        case "W", "w" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          paramBuilder.flushStudentToMove();

          // Takes the color
          (new MenuStudentColor()).makeChoice(paramBuilder);

          // Ask for amount
          int amount;
          try {
            System.out.print("Amount: ");
            amount = s.nextInt();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
          } catch (InputMismatchException e) {
            System.out.println("Insert a number");
          }

          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Send actions
          if (!controller.sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
            System.out.println("Invalid input parameters");
          }
        }
        // Choose a character card from those in playing field
        case "E", "e" -> {
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.EFFECT))
            break;
          int ccIndex = -1;
          try {
            System.out.println("Playable character cards: ");
            (new CharacterCardView(controller.getGameState().getPlayingField().getCharacterCards())).draw(System.out);
            System.out.println("Choose a character card: ");
            ccIndex = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Input must be a number");
            return;
          }
          if (!controller.sendChooseCharacterCard(ccIndex)) {
            System.out.println("Invalid input parameters");
            return;
          }
          (new MenuEffect(controller)).makeChoice(paramBuilder);
          done = true;
        }

        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return new MenuMoving(controller);
  }
}
