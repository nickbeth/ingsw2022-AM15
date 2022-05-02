package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;

public class ActivateCCEffect implements GameAction {
  private CharacterCard cc;

  //todo gestione del prezzo della CC
  public ActivateCCEffect(CharacterCard cc) {
    this.cc = cc;
  }

  @Override
  public void apply(GameState gameState) {
    gameState.getPlayingField().setPlayedCharacterCard(cc);
    gameState.getPlayingField().getPlayedCharacterCard().applyEffect(gameState);
    gameState.advanceTurnPhase();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return cc.getCardEnum().equals(gameState.getPlayingField().getPlayedCharacterCard().getCardEnum());
  }
}
