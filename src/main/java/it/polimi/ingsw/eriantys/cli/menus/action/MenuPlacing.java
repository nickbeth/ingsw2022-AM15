package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.cli.views.CharacterCardView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPlacing extends Menu {
  private int studentMoved = 0;

  public MenuPlacing(Controller controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions();
    int studentsLeft = controller.getGameState().getRuleBook().playableStudentCount - studentMoved;
    if (controller.getNickname().equals(controller.getGameState().getCurrentPlayer().getNickname())) {
      if (controller.getGameState().getTurnPhase() == TurnPhase.PLACING) {
        out.println(
                MessageFormat.format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
        out.println(
                MessageFormat.format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
        out.println("E - Play a character card");
      }
    }
  }

  @Override
  public void show(Scanner in, PrintStream out) {
    boolean done = false;
    ParamBuilder paramBuilder = new ParamBuilder();

    do {
      showOptions(out);
      switch (in.nextLine()) {
        // Move Students from entrance to island
        case "Q", "q" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          paramBuilder.flushStudentToMove();

          // Shows entrance
          (new DashboardView(controller.getGameState().getRuleBook(), controller.getGameState().getCurrentPlayer().getDashboard(),
                  controller.getGameState().getPlayingField().getProfessorHolder())).draw(out);

          // Takes the color
          (new MenuStudentColor()).show(in, out, paramBuilder);

          // Ask for amount
          int amount;
          try {
            out.print("Amount: ");
            amount = in.nextInt();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
          } catch (InputMismatchException e) {
            out.println("Insert a number");
          }

          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Takes island input and send the action
          int islandIndex = -1;
          try {
            // Shows islands
            out.println("Choose an island: ");
            (new IslandsView(controller.getGameState().getPlayingField().getIslands(),
                    controller.getGameState().getPlayingField().getMotherNaturePosition())).draw(out);
            islandIndex = in.nextInt();
          } catch (InputMismatchException e) {
            out.println("Number required");
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
          }
          // Send actions
          if (!controller.sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
            out.println("Invalid input parameters");
          }
        }
        // Move Students from entrance to dining
        case "W", "w" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          paramBuilder.flushStudentToMove();

          // Takes the color
          (new MenuStudentColor()).show(in, out, paramBuilder);

          // Ask for amount
          int amount;
          try {
            out.print("Amount: ");
            amount = in.nextInt();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
          } catch (InputMismatchException e) {
            out.println("Insert a number");
          }

          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Send actions
          if (!controller.sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
            studentMoved -= paramBuilder.getStudentsToMove().getCount();
            out.println("Invalid input parameters");
          }
        }
        // Choose a character card from those in playing field
        case "E", "e" -> {
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.EFFECT))
            break;
          int ccIndex = -1;
          try {
            out.println("Playable character cards: ");
            (new CharacterCardView(controller.getGameState().getPlayingField().getCharacterCards())).draw(out);
            out.println("Choose a character card: ");
            ccIndex = in.nextInt();
          } catch (InputMismatchException e) {
            out.println("Input must be a number");
            return;
          }
          if (!controller.sendChooseCharacterCard(ccIndex)) {
            out.println("Invalid input parameters");
            return;
          }
          (new MenuEffect(controller)).show(in, out);
          done = true;
        }

        default -> out.println("Choose a valid option");
      }
    } while (!done);
  }

  @Override
  public Menu next() {
    return new MenuMoving(controller);
  }
}
