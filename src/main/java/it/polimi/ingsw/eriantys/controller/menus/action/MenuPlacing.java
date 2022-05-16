package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.CharacterCardView;
import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.controller.menus.planning.MenuPickAssistantCard;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPlacing extends Menu {
  private int studentMoved = 0;
  
  public MenuPlacing(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }
  
  @Override
  public void showOptions() {
    showViewOptions();
    int studentsLeft = game.getRuleBook().playableStudentCount - studentMoved;
    if (playerNickname.equals(game.getCurrentPlayer().getNickname())) {
      if (game.getTurnPhase() == TurnPhase.PLACING) {
        System.out.println(
                MessageFormat.format("Q - Move a student from entrance to island ({0} left)", studentsLeft));
        System.out.println(
                MessageFormat.format("W - Move a student from entrance to dining ({0} left)", studentsLeft));
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
          if (!game.getTurnPhase().equals(TurnPhase.PLACING))
            break;
          
          paramBuilder.flushStudentToMove();
          
          // Shows entrance
          (new DashboardView(game.getRuleBook(), game.getCurrentPlayer().getDashboard(),
                  game.getPlayingField().getProfessorHolder())).draw(System.out);
          
          // Takes the color
          (new MenuColor()).makeChoice(paramBuilder);
          
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
            (new IslandsView(game.getPlayingField().getIslands(),
                    game.getPlayingField().getMotherNaturePosition())).draw(System.out);
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
          if (!game.getTurnPhase().equals(TurnPhase.PLACING))
            break;
          
          paramBuilder.flushStudentToMove();
          
          // Takes the color
          (new MenuColor()).makeChoice(paramBuilder);
          
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
          if (!game.getTurnPhase().equals(TurnPhase.EFFECT))
            break;
          int ccIndex = -1;
          try {
            System.out.println("Playable character cards: ");
            (new CharacterCardView(game.getPlayingField().getCharacterCards())).draw(System.out);
            System.out.println("Choose a character card: ");
            ccIndex = s.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Input must be a number");
          }
          if (!controller.sendChooseCharacterCard(ccIndex)) {
            System.out.println("Invalid input parameters");
          }
          (new MenuEffect(game, playerNickname, controller)).makeChoice(paramBuilder);
        }
        
        default -> System.out.println("Choose a valid option");
      }
    } while (!done);
  }
  
  @Override
  public Menu nextMenu() {
    return new MenuPickAssistantCard(game, playerNickname, controller);
  }
}
