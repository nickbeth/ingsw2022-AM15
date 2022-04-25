package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public class ForceMotherNatureEffects implements CharacterCard {
  private final int islandIndex;
  private static int cost = 1;
  public ForceMotherNatureEffects(int islandIndex) {
    this.islandIndex = islandIndex;
  }

  @Override
  public boolean requiresInput() {
    return true;
  }

  @Override
  public int getCost() {
    return cost;
  }


  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    cost = 2;
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);
    gameService.applyMotherNatureEffect(islandIndex, gameState.getPlayingField(), gameState.getPlayers());
  }

  /**
   * Checks:<br>
   * - if the chosen index is allowed<br>
   * - if player has enough coins
   * @param gameState
   * @return
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getPlayingField().getIslandsAmount() > islandIndex &&
            islandIndex >= 0 &&
            gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
