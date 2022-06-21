package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;
import java.util.Objects;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class MenuPlacing extends MenuGame {

  public MenuPlacing() {
    super();
    showOptions();
  }

  private int studentsLeftToMove() {
    int myCount = me().getDashboard().getEntrance().getCount();
    int finalCount = rules().entranceSize - rules().playableStudentCount;
    return myCount - finalCount;
  }

  private boolean isCharacterCardPlayed() {
    return game().getPlayingField().getPlayedCharacterCard() != null;
  }

  @Override
  protected void showOptions() {
    showViewOptions(out);

    if (isMyTurn()) {
      if (studentsLeftToMove() != 0) {
        out.println(MessageFormat
            .format("Q - Move a student from entrance to island ({0} left)", studentsLeftToMove()));
        out.println(MessageFormat
            .format("W - Move a student from entrance to dining ({0} left)", studentsLeftToMove()));
      }
      if (rules().gameMode.equals(GameMode.EXPERT) && !isCharacterCardPlayed())
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
            if (!game().getTurnPhase().equals(TurnPhase.PLACING))
              break;

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

            // Advance game condition
            if (Objects.equals(escapeCondition(), MenuEnum.MOVING))
              return MenuEnum.MOVING;
          }

          // Move Students from entrance to dining
          case "W", "w" -> {
            // Check of the Turn phase
            if (!game().getTurnPhase().equals(TurnPhase.PLACING))
              break;

            chooseColorAndAmount(paramBuilder);

            // Send actions
            if (!controller.sender().sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
              out.println("Invalid input parameters");
              showOptions();
              break;
            }

            waitForGreenLight();

            // Advance game condition
            if (Objects.equals(escapeCondition(), MenuEnum.MOVING))
              return MenuEnum.MOVING;
          }

          // Choose a character card from those in playing field
          // TODO: sposta il blocco istruzioni in MenuEffect
          // TODO: testare lo spostamento
          case "E", "e" -> {
            if(!isCharacterCardPlayed())
              return MenuEnum.EFFECT;
            out.println("A card was already played");
          }

          default -> {
          }
        }
      }
    }
  }

  private MenuEnum escapeCondition() {
//    int playableStudents = rules().playableStudentCount;

    // Condition to continue the game
    if (studentsLeftToMove() == 0) {
      // Ask the player if he wants to play and effect before going on with the game
      if (rules().gameMode.equals(GameMode.EXPERT)) {
        out.println("\nDo you want to play a character card?");
        out.println("1 - YES");
        out.println("ANY_KEY - NO");
        out.print("Make a choice: ");
        // If so let him the chance to play a character card
        if (getKeyboardInput().equals("1")) {
          showOptions();
          return null;
        }
      }
      return MenuEnum.MOVING;
    } else if (studentsLeftToMove() < 0) {
      clientLogger.error("Error implementing placing students");
      showOptions();
    }

    return null;
  }

  private void chooseColorAndAmount(ParamBuilder paramBuilder) {
    // Clear previously sent students
    paramBuilder.flushStudentToMove();

    // Shows entrance
    new DashboardView(me(), rules(), professorHolder()).draw(out);

    // Takes the color
    new MenuStudentColor().show(paramBuilder);

    // Ask for amount
    out.print("Amount: ");
    while (true) {
      int amount = getNumber();
      if (amount <= studentsLeftToMove()) {
        paramBuilder.addStudentColor(paramBuilder.getChosenColor(), amount);
        break;
      }
      out.println("Cannot move that amount. Student left to move: " + studentsLeftToMove() + ".");
      out.print("Insert again: ");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
