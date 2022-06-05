package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;

public class ReInitiateGame extends GameAction {
  private final GameState gameCopy;

  public ReInitiateGame(GameState gameCopy) {
    this.gameCopy = gameCopy;
  }

  @Override
  public void apply(GameState gameState) {

    // ReInitiate players
    gameState.setPlayers(gameCopy.getPlayers());
    gameState.setPlanOrder(gameCopy.getPlanOrder());
    gameState.setTurnOrder(gameCopy.getTurnOrder());
    gameState.setCurrentPlayer(gameCopy.getCurrentPlayer());

    // ReInitiate phases
    gameState.setGamePhase(gameCopy.getGamePhase());
    gameState.setTurnPhase(gameCopy.getTurnPhase());

    // ReInitiate playing field
    gameState.setPlayingField(gameCopy.getPlayingField());

    // ReInitiate rules
    gameState.setRuleBook(gameCopy.getRuleBook());
  }

  @Override
  public boolean isValid(GameState gameState) {
    return false;
  }
}
