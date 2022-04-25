package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public class ForceMotherNatureEffects implements CharacterCard {
  private int islandIndex;

  public ForceMotherNatureEffects(int islandIndex) {
    this.islandIndex = islandIndex;
  }

  @Override
  public boolean requiresInput() {
    return true;
  }


  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    gameService.applyMotherNatureEffect(islandIndex, gameState.getPlayingField(), gameState.getPlayers());
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
