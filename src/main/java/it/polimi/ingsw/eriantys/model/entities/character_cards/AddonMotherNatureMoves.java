package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;

public class AddonMotherNatureMoves implements CharacterCard {
  private final static int MOVES = 2;

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    gameState.getCurrentPlayer().addToMaxMovement(MOVES);
  }

  @Override
  public int getCost() {
    return 0;
  }

  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public boolean isValid(GameState gameState) {
    return true;
  }
}
