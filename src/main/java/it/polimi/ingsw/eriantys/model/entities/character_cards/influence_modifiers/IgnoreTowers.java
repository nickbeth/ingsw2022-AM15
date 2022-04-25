package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

public class IgnoreTowers implements InfluenceModifierCC, CharacterCard {
  private Island island;
  private TowerColor team;

  public IgnoreTowers(Island island, TowerColor team) {
    this.island = island;
    this.team = team;
  }

  @Override
  public Integer applyModifier(Integer influence) {
    int modifier = 0;
    // Minus number of tower on that island for the team conqueror
    if (island.getTowerColor() == team) {
      modifier -= island.getTowerCount();
    }
    return influence + modifier;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {

  }

  @Override
  public boolean requiresInput() {
    return false;
  }

  @Override
  public boolean isValid() {
    return false;
  }
}
