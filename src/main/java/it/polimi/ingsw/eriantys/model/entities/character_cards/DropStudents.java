package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.ArrayList;
import java.util.List;

public class DropStudents implements CharacterCard {
  private HouseColor colorToBeDropped;
  private static int DROP_STUDENTS_AMOUNT = 3;

  public DropStudents(HouseColor colorToBeDropped) {
    this.colorToBeDropped = colorToBeDropped;
  }

  /**
   * Removes 3 students of a certain HouseColor from the entrance of each player
   * then advances to next TurnPhase;
   */
  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    List<Students> diningHallList = new ArrayList<>();
    gameState.getPlayers().forEach((player) ->
            diningHallList.add(player.getDashboard().getDiningHall())
    );
    StudentBag bag = gameState.getPlayingField().getStudentBag();
    gameService.dropStudents(diningHallList, colorToBeDropped, DROP_STUDENTS_AMOUNT, bag);
  }

  @Override
  public boolean requiresInput() {
    return true;
  }

  public CharacterCard myType(GameState gameState, IGameService gameService) {
    return new DropStudents(HouseColor.PINK);
  }

//  @Override
//  public CharacterCard setInput(Input input) {
//    input.getPlayerInput();
//    HouseColor color = HouseColor.PINK;
//    return new DropStudents(color);
//  }

  @Override
  public boolean isValid() {
    return true;
  }
}
