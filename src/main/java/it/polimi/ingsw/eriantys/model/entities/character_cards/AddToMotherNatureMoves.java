package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public class AddToMotherNatureMoves implements CharacterCard {
  private final static int MOVES = 2;
  private static int PRICE = 3;
  private static int cost = 1;

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    cost = 2;
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);
    gameState.getCurrentPlayer().addToMaxMovement(MOVES);
  }

  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public int getCost() {
    return cost;
  }

  /**
   * Checks:
   *  - if player has enough coins
   * @param gameState
   * @return
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
