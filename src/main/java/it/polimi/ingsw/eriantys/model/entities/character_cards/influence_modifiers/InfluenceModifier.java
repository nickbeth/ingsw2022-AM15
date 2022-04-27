package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCardEnum;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import static it.polimi.ingsw.eriantys.model.entities.character_cards.CardEffectsService.getCardEffectsService;


public class InfluenceModifier implements CharacterCard {
  private final InfluenceModifierCC modifyInfluence;
  private final CharacterCardEnum card;
  private HouseColor ignoredColor = null;

  public InfluenceModifier(InfluenceModifierCC modifyInfluence, CharacterCardEnum card) {
    this.modifyInfluence = modifyInfluence;
    this.card = card;
  }

  public InfluenceModifier(InfluenceModifierCC modifyInfluence, CharacterCardEnum card, HouseColor ignoredColor) {
    this.modifyInfluence = modifyInfluence;
    this.card = card;
  }

  public void setIgnoredColor(HouseColor ignoredColor) {
    this.ignoredColor = ignoredColor;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    modifyInfluence.applyModifier(gameState, getCardEffectsService());
    gameState.getCurrentPlayer().removeCoins(card.getCost());
    gameState.getPlayingField().addCoinsToBank(card.getCost());
  }

  @Override
  public int getCost() {
    return card.getCost();
  }

  @Override
  public boolean requiresInput() {
    return card.isRequiredInput();
  }

  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= card.getCost();
  }
}
