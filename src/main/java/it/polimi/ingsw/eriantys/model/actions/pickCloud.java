package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class pickCloud extends PlayerAction {
  int cloudIndex;
  public pickCloud(String nickname, int index) {
    this.cloudIndex = index;
    this.playerNickname = nickname;
  }


  /**
   * Gets students from pickedCloud and puts them onto the players entrance,then advances player , turnPhase
   * and if its the last player it advances GamePhase
   * @param gameState
   */
  @Override
  public void apply(GameState gameState){
    Cloud cloud = gameState.getPlayingField().getCloud(cloudIndex);
    gameState.getCurrentPlayer().getDashboard().addToEntrance(cloud.getStudents());
    cloud.setStudents(new Students());

    gameState.advanceTurnPhase();
    Player lastPlayer = gameState.getTurnOrder().get(gameState.getTurnOrder().size() - 1);
    if(gameState.getCurrentPlayer().equals(lastPlayer)){
      gameState.advanceGamePhase();
    }
    gameState.advancePlayer();
  }

  /**
   * checks:
   * If currentPlayer is the player who did the action</br>
   * If the gamePhase is ACTION</br>
   * If the turnPhase is PICKING</br>
   * If the cloud index is allowed
   * If the picked cloud is empty</br>
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.PICKING &&
            cloudIndex >= 0 &&
            cloudIndex < gameState.getRuleBook().cloudCount &&
            !gameState.getPlayingField().getCloud(cloudIndex).isEmpty();
  }
}
