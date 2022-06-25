package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;

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

  private boolean isCharacterCardUnplayed() {
    return game().getPlayingField().getPlayedCharacterCard() == null;
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
      if (rules().gameMode.equals(GameMode.EXPERT) && isCharacterCardUnplayed())
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
      if (handleDisconnection(choice))
        return MenuEnum.CREATE_OR_JOIN;

      if (isMyTurn()) {
        switch (choice) {
          case "forced_advancement_to_next_menu" -> {
            return null;
          }

          // Move Students from entrance to island
          case "Q", "q" -> {

            // Check of the Turn phase
            if (!turnPhase().equals(TurnPhase.PLACING) || !gamePhase().equals(GamePhase.ACTION)) {
              out.println(colored("You're in the wrong phase.", RED));
              break;
            }

            chooseColorAndAmount(paramBuilder);

            // Shows islands
            islandsView.draw(out);

            // Take island index input
            out.println("Choose an island: ");
            int islandIndex = getNumber() - 1; // Index correction

            // Send actions
            if (!controller.sender().sendMoveStudentsToIsland(paramBuilder.getStudentsToMove(), islandIndex)) {
              out.println(colored("Invalid input parameters", RED));
              showOptions();
              break;
            }
            waitForGreenLight();

            // Advance game condition
            return escapeCondition();
          }

          // Move Students from entrance to dining
          case "W", "w" -> {
            // Check of the Turn phase
            if (!game().getTurnPhase().equals(TurnPhase.PLACING))
              break;

            chooseColorAndAmount(paramBuilder);

            // Send actions
            if (!controller.sender().sendMoveStudentsToDiningHall(paramBuilder.getStudentsToMove())) {
              out.println(colored("Invalid input parameters", RED));
              showOptions();
              break;
            }

            waitForGreenLight();

            // Advance game condition
            return escapeCondition();
          }

          // Choose a character card from those in playing field
          case "E", "e" -> {
            if (!isCharacterCardUnplayed()) {
              new MenuEffect().show();
              return MenuEnum.EFFECT;
            }
            out.println(colored("A card was already played", RED));
          }

          default -> {
          }
        }
      }
    }
  }

  private MenuEnum escapeCondition() {
    // Condition to continue the game
    if (studentsLeftToMove() == 0) {
      // Ask the player if he wants to play and effect before going on with the game
      if (rules().gameMode.equals(GameMode.EXPERT) && !isCharacterCardUnplayed()) {
        out.println("\nDo you want to play a character card?");
        out.println("1 - YES");
        out.println("ANY_KEY - NO");
        out.print("Make a choice: ");

        // Go to Menu Effect
        if (getKeyboardInput().equals("1")) {
          return MenuEnum.EFFECT;
        }
      }
      return MenuEnum.MOVING;
    }

    showOptions();
    return MenuEnum.PLACING;
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
      out.println(colored("Cannot move that amount. Student left to move: " + studentsLeftToMove() + ".", RED));
      out.print("Insert again: ");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
