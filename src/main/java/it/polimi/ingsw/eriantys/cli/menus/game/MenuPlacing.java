package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CharacterCardsView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

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
    int studentsLeft = rules().playableStudentCount - studentMoved;

    showViewOptions(out);

    if (isMyTurn()) {
      out.println(MessageFormat
          .format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
      out.println(MessageFormat
          .format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
      if (rules().gameMode.equals(GameMode.EXPERT) && !ccUsed)
        out.println("E - Play a character card");
    }
    out.print("Make option: ");
  }

  @Override
  public MenuEnum show() {
    ParamBuilder paramBuilder = new ParamBuilder();

    while (true) {

      String choice = getNonBlankString();

      handleViewOptions(choice);

      if (isMyTurn()) {
        switch (choice) {

          // Move Students from entrance to island
          case "Q", "q" -> {
            // Check of the Turn phase
//            if (!game().getTurnPhase().equals(TurnPhase.PLACING))
//              break;

            chooseColorAndAmount(paramBuilder);

            // Shows islands
            new IslandsView(islands(), motherPosition()).draw(out);

            // Take island index input
            out.println("Choose an island: ");
            int islandIndex = getNumber() - 1; // Index correction

            // Send actions
            if (!controller.sender().sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
              out.println("Invalid input parameters");
              showOptions();
              break;
            }
            waitForGreenLight();

            // Update students moved
            studentMoved += paramBuilder.getStudentsToMove().getCount();

            // Escape condition
            int playableStudents = rules().playableStudentCount;
            if (studentMoved == playableStudents)
              return MenuEnum.MOVING;
            else if (studentMoved > playableStudents) {
              studentMoved -= paramBuilder.getStudentsToMove().getCount();
              clientLogger.error("Error implementing placing students");
              showOptions();
            }
          }

          // Move Students from entrance to dining
          case "W", "w" -> {
            // Check of the Turn phase
//            if (!game().getTurnPhase().equals(TurnPhase.PLACING))
//              break;

            chooseColorAndAmount(paramBuilder);

            // Send actions
            if (!controller.sender().sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
              out.println("Invalid input parameters");
              showOptions();
              break;
            }

            waitForGreenLight();

            // Update students moved
            studentMoved += paramBuilder.getStudentsToMove().getCount();

            // Escape condition
            int playableStudents = rules().playableStudentCount;
            if (studentMoved == playableStudents)
              return MenuEnum.MOVING;
            else if (studentMoved > playableStudents) {
              studentMoved -= paramBuilder.getStudentsToMove().getCount();
              clientLogger.error("Error implementing placing students");
              showOptions();
            }
          }

          // Choose a character card from those in playing field
          case "E", "e" -> {

            // Check if the card has already been used
            if (ccUsed) break;

            // Show playable CC
            List<CharacterCard> playableCC = controller.getGameState().getPlayingField().getCharacterCards();
            out.println("Playable character cards: ");
            (new CharacterCardsView(playableCC)).draw(out);

            // Choose CC
            out.println("Choose a character card: ");
            int ccIndex = getNumber();

            // Send the action
            if (!controller.sender().sendChooseCharacterCard(ccIndex)) {
              out.println("Invalid input parameters");
              showOptions();
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

  private void chooseColorAndAmount(ParamBuilder paramBuilder) {
    // Clear previously sent students
    paramBuilder.flushStudentToMove();

    // Shows entrance
    new DashboardView(me(), rules(), professorHolder()).draw(out);

    // Takes the color
    new MenuStudentColor().show(paramBuilder);

    // Ask for amount
    while (true) {
      out.print("Amount: ");
      int amount = getNumber();
      if (amount + studentMoved <= rules().playableStudentCount) {
        paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
        break;
      }
      out.println("Cannot move that amount. Student left to move: " + (rules().playableStudentCount - studentMoved) + ".");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
