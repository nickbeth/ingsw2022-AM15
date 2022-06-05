package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CharacterCardView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;
import java.util.List;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class MenuPlacing extends MenuGame {
  private int studentMoved = 0;
  private boolean ccUsed = false;

  public MenuPlacing() {
    super();
    showOptions();
  }

  @Override
  protected void showOptions() {
    showViewOptions(out);
    int studentsLeft = rules.playableStudentCount - studentMoved;

    clearConsole();

    if (controller.getGameState().isTurnOf(controller.getNickname())) {
      out.println(MessageFormat
          .format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
      out.println(MessageFormat
          .format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
      if (rules.gameMode.equals(GameMode.EXPERT) && !ccUsed)
        out.println("E - Play a character card");
    } else {
      out.println("It's not your turn, you can see the state of the game tho.");
    }
  }

  @Override
  public MenuEnum show() {
    ParamBuilder paramBuilder = new ParamBuilder();

    while (true) {

      String choice = getNonBlankString();

      handleViewOptions(choice);

      if (controller.getGameState().isTurnOf(controller.getNickname())) {
        switch (choice) {

          // Move Students from entrance to island
          case "Q", "q" -> {
            // Check of the Turn phase
            if (!game.getTurnPhase().equals(TurnPhase.PLACING))
              break;

            // Clear previously sent students
            paramBuilder.flushStudentToMove();

            // Shows entrance
            new DashboardView(rules, game.getCurrentPlayer().getDashboard(), professorHolder).draw(out);

            // Takes the color
            new MenuStudentColor().show(in, out, paramBuilder);

            // Ask for amount
            out.print("Amount: ");
            int amount = getNumber();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);

            // Shows islands
            (new IslandsView(islands, motherPosition)).draw(out);

            // Take island index input
            out.println("Choose an island: ");
            int islandIndex = getNumber();

            // Send actions
            if (!controller.sender().sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
              out.println("Invalid input parameters");
              break;
            }

            waitForGreenLight();

            // Memorize new students moved
            studentMoved += paramBuilder.getStudentsToMove().getCount();

            // Escape condition
            int playableStudents = rules.playableStudentCount;
            if (studentMoved == playableStudents)
              return MenuEnum.MOVING;
            else if (studentMoved > playableStudents) {
              clientLogger.error("Error implementing placing students");
            }
          }

          // Move Students from entrance to dining
          case "W", "w" -> {

            // Check of the Turn phase
            if (!game.getTurnPhase().equals(TurnPhase.PLACING))
              break;

            // Clear previously sent students
            paramBuilder.flushStudentToMove();

            // Shows entrance
            new DashboardView(rules, currentPlayer.getDashboard(), professorHolder).draw(out);

            // Takes the color
            new MenuStudentColor().show(in, out, paramBuilder);

            // Clear previously sent students
            paramBuilder.flushStudentToMove();

            // Takes the color
            (new MenuStudentColor()).show(in, out, paramBuilder);

            // Ask for amount
            out.print("Amount: ");
            int amount = getNumber();
            paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);


            // Send actions
            if (!controller.sender().sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
              out.println("Invalid input parameters");
            }

            waitForGreenLight();

            // Memorize new students moved
            studentMoved += paramBuilder.getStudentsToMove().getCount();

            // Escape condition
            int playableStudents = rules.playableStudentCount;
            if (studentMoved == playableStudents)
              return MenuEnum.MOVING;
            else if (studentMoved > playableStudents) {
              clientLogger.error("Error implementing placing students");
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
            int ccIndex = getNumber();

            // Send the action
            if (!controller.sender().sendChooseCharacterCard(ccIndex)) {
              out.println("Invalid input parameters");
              break;
            }
            waitForGreenLight();
            if ((new MenuEffect()).show() != null)
              ccUsed = true;
          }

          default -> {
          }
        }
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
