package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;

import java.util.List;

public class PlaceStudents extends PlayerAction {
  private final List<StudentMovement> entries;

  public PlaceStudents(List<StudentMovement> entries, String nickname) {
    this.playerNickname = nickname;
    this.entries = entries;
  }

  @Override
  public void apply(GameState gameState) {

    for (StudentMovement move : entries) {
      switch (move.src) {
        case ENTRANCE -> gameState.
                getCurrentPlayer().getDashboard().getEntrance().tryRemoveStudent(move.studentColor);
        case DINIGN -> gameState.
                getCurrentPlayer().getDashboard().getDiningHall().tryRemoveStudent(move.studentColor);
        default -> throw new IllegalStateException("Unexpected value: " + move.src);
      }
      switch (move.dest) {
        case ENTRANCE -> gameState.
                getCurrentPlayer().getDashboard().getEntrance().addStudent(move.studentColor);
        case DINIGN -> gameState.
                getCurrentPlayer().getDashboard().getDiningHall().addStudent(move.studentColor);
        case ISLAND -> gameState.
                getPlayingField().getIsland(move.islandIndex).getStudents().addStudent(move.studentColor);
//        case CARD -> gameState.
//                getPlayingField().getCard();
        default -> throw new IllegalStateException("Unexpected value: " + move.src);
      }
    }
  }

  @Override
  public boolean isValid(GameState gameState) {
    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    for (StudentMovement move : entries) {
      switch (move.src) {
        case ENTRANCE -> {
          if (gameState.getCurrentPlayer().getDashboard().getEntrance().getCount(move.studentColor) == 0)
            return false;
        }
        case DINIGN -> {
          if (gameState.getCurrentPlayer().getDashboard().getDiningHall().getCount(move.studentColor) == 0)
            return false;
        }
        case CARD -> {
//          if (gameState.getCard().getStudents().getCount(move.studentColor) == 0)
//            return false;
        }
        case ISLAND -> {
          return false;
        }
        default -> throw new IllegalStateException("Unexpected value: " + move.src);
      }
    }
    return true;
  }
}
