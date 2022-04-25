package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.TeamsInfluenceTracer;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class IgnoreColor implements InfluenceModifierCC, CharacterCard {
  private HouseColor ignoredColor;
  private final static int BASE_COST = 3;
  private final static int INCREMENTED_COST = 4;
  private static int cost = BASE_COST;


  public IgnoreColor(HouseColor ignoredColor) {
    this.ignoredColor = ignoredColor;
  }

  @Override
  public int getCost() {
    return cost;
  }

  // todo test
  @Override
  public void applyModifier(GameState gameState) {
    List<Island> islands = gameState.getPlayingField().getIslands();
    TowerColor teamOwningIgnoredProfessor = gameState.getPlayingField().getProfessorHolder().getProfessorOwner(ignoredColor);

    // For each island updates teams' influence based on the ignored color
    islands.forEach((island) -> {
      TeamsInfluenceTracer influenceTracer = island.getTeamsInfluenceTracer();
      Integer ignoredStudentsCount = island.getStudents().getCount(ignoredColor);
      influenceTracer.setInfluence(teamOwningIgnoredProfessor, influenceTracer.getInfluence(teamOwningIgnoredProfessor) - ignoredStudentsCount);
    });
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {
    applyModifier(gameState);
    gameState.getCurrentPlayer().removeCoins(cost);
    gameState.getPlayingField().addCoinsToBank(cost);
    cost = INCREMENTED_COST;
  }

  @Override
  public boolean requiresInput() {
    return true;
  }

  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
