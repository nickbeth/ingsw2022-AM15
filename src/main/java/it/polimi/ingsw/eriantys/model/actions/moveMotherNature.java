package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.Optional;

public class moveMotherNature extends PlayerAction {
  int amount;

  public moveMotherNature(String nickname, int amount) {
    this.playerNickname = nickname;
    this.amount = amount;
  }

  /**
   * Moves motherNature
   * if the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands.
   * if necessary it will advance gamePhase and turnPhase
   * @param gameState
   */
  @Override
  public void apply(GameState gameState) {
    PlayingField playingField = gameState.getPlayingField();
    playingField.moveMotherNature(amount);
    int motherNaturePos = playingField.getMotherNaturePosition();

    if (playingField.getIsland(motherNaturePos).isLocked()) {
      playingField.getIsland(motherNaturePos).setLocked(false);
      //TODO lock returns to the characterCard
    } else {
      Optional<TowerColor> mostInfluential = playingField.getMostInfluential(motherNaturePos);
      if(mostInfluential.isPresent()){
        playingField.getIsland(motherNaturePos).setTowerColor(mostInfluential.get());
        playingField.mergeIslands(motherNaturePos);
      }
    }

    //checks if the current player is the last in turn order and subsequently changes phases
    Player lastPlayer = gameState.getTurnOrder().get(gameState.getTurnOrder().size() - 1);
    if(gameState.getCurrentPlayer().equals(lastPlayer)){
      gameState.advanceTurnPhase();
      gameState.advanceGamePhase();
    }
    gameState.advancePlayer();
  }

  /**
   * checks:</br>
   * If current player is the player who did the action</br>
   * If the gamePhase is ACTION</br>
   * If the turnPhase is MOVING</br>
   * If the amount of movements is allowed</br>
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    if (!gameState.getCurrentPlayer().getNickname().equals(playerNickname)) return false;
    if (!(gameState.getGamePhase() == GamePhase.ACTION)) return false;
    if (!(gameState.getTurnPhase() == TurnPhase.MOVING)) return false;
    if (gameState.getCurrentPlayer().getTurnPriority() < amount) return false;
    return true;
  }
}
