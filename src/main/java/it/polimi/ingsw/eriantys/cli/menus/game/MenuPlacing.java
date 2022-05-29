package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CharacterCardView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class MenuPlacing extends MenuGame {
  private int studentMoved = 0;
  private boolean ccUsed = false;

  public MenuPlacing() {
    super();
  }

  @Override
  protected void showOptions(PrintStream out) {
    showViewOptions(out);
    int studentsLeft = controller.getGameState().getRuleBook().playableStudentCount - studentMoved;

    clearConsole();

    if (controller.getGameState().getTurnPhase() == TurnPhase.PLACING) {
      out.println(MessageFormat
          .format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
      out.println(MessageFormat
          .format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
      if (controller.getGameInfo().getMode().equals(GameMode.EXPERT))
        out.println("E - Play a character card");
    }
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {
    ParamBuilder paramBuilder = new ParamBuilder();

    while (true) {
      showOptions(out);

      String choice = getNonBlankString(in, out);

      handleViewOptions(choice, out);
      switch (choice) {

        // Move Students from entrance to island
        case "Q", "q" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          // Clear previously sent students
          paramBuilder.flushStudentToMove();

          // Shows entrance
          new DashboardView(rules, game.getCurrentPlayer().getDashboard(), professorHolder).draw(out);

          // Takes the color
          new MenuStudentColor().show(in, out, paramBuilder);

          // Ask for amount
          out.print("Amount: ");
          int amount = getNumber(in, out);
          paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);

          // Shows islands
          (new IslandsView(islands, motherPosition)).draw(out);

          // Take island index input
          out.println("Choose an island: ");
          int islandIndex = getNumber(in, out);

          // Send actions
          if (!controller.sender().sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
            out.println("Invalid input parameters");
          }

          waitForGreenLight();

          // Memorize new students moved
          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Escape condition
          int playableStudents = controller.getGameState().getRuleBook().playableStudentCount;
          if (studentMoved == playableStudents)
            return MenuEnum.MOVING;
          else if (studentMoved > playableStudents) {
            Logger.error(new RuntimeException(), "Error implementing placing students");
          }
        }

        // Move Students from entrance to dining
        case "W", "w" -> {
          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          List<Island> islands = controller.getGameState().getPlayingField().getIslands();
          int motherNaturePosition = controller.getGameState().getPlayingField().getMotherNaturePosition();

          // Check of the Turn phase
          if (!controller.getGameState().getTurnPhase().equals(TurnPhase.PLACING))
            break;

          // Clear previously sent students
          paramBuilder.flushStudentToMove();

          // Shows entrance
          (new DashboardView(controller.getGameState().getRuleBook(), controller.getGameState().getCurrentPlayer().getDashboard(),
              controller.getGameState().getPlayingField().getProfessorHolder())).draw(out);

          // Takes the color
          (new MenuStudentColor()).show(in, out, paramBuilder);

          // Clear previously sent students
          paramBuilder.flushStudentToMove();

          // Takes the color
          (new MenuStudentColor()).show(in, out, paramBuilder);

          // Ask for amount
          out.print("Amount: ");
          int amount = getNumber(in, out);
          paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);


          // Send actions
          if (!controller.sender().sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
            out.println("Invalid input parameters");
          }

          waitForGreenLight();

          // Memorize new students moved
          studentMoved += paramBuilder.getStudentsToMove().getCount();

          // Escape condition
          int playableStudents = controller.getGameState().getRuleBook().playableStudentCount;
          if (studentMoved == playableStudents)
            return MenuEnum.MOVING;
          else if (studentMoved > playableStudents) {
            Logger.error(new RuntimeException(), "Error implementing placing students");
          }
        }

        // Choose a character card from those in playing field
        case "E", "e" -> {

          // Check if the card has already been used
          if (ccUsed) break;

          // Show playable CC
          List<CharacterCard> playableCC = controller.getGameState().getPlayingField().getCharacterCards();
          out.println("Playable character cards: ");
          (new CharacterCardView(playableCC)).draw(out);

          // Choose CC
          out.println("Choose a character card: ");
          int ccIndex = getNumber(in, out);

          // Send the action
          if (!controller.sender().sendChooseCharacterCard(ccIndex)) {
            out.println("Invalid input parameters");
            break;
          }
          waitForGreenLight();
          (new MenuEffect()).show(in, out);
          ccUsed = true;
        }

        default -> out.println("Choose a valid option");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
