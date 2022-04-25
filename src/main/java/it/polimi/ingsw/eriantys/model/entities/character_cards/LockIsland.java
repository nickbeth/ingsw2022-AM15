package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public class LockIsland implements CharacterCard {
  private final int index;
  private static int cost = 2;

  public LockIsland(String nickname, int index) {
    this.index = index;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    cost = 3;
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);
    gameService.lockIsland(gameState.getPlayingField().getIsland(index));
  }

  @Override
  public boolean requiresInput() {
    return true;
  }

  @Override
  public int getCost() {
    return cost;
  }

  /**
   * Checks:<br>
   * - if the chosen index is allowed<br>
   * - if the chosen island is already locked<br>
   * - if player has enough coins
   *
   * @param gameState
   * @return
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getPlayingField().getIslandsAmount() > index &&
            index >= 0 &&
            !gameState.getPlayingField().getIsland(index).isLocked() &&
            gameState.getCurrentPlayer().getCoins() >= cost;

  }
}
