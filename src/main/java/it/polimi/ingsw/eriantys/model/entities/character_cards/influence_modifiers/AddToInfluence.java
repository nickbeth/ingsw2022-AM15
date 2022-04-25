package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.TeamsInfluenceTracer;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class AddToInfluence implements InfluenceModifierCC, CharacterCard {
  private static final Integer MODIFIER = 2;
  private final static int BASE_COST = 2;
  private final static int INCREMENTED_COST = 3;
  private static int cost = BASE_COST;

  @Override
  public void applyModifier(GameState gameState) {
    List<TeamsInfluenceTracer> teamsInfluenceList = new ArrayList<>();

    gameState.getPlayingField().getIslands().forEach((island) ->
            teamsInfluenceList.add(island.getTeamsInfluenceTracer()));

    TowerColor currTeam = gameState.getCurrentPlayer().getColorTeam();

    teamsInfluenceList.forEach((teamsInfluence) ->
            teamsInfluence.setInfluence(currTeam, teamsInfluence.getInfluence(currTeam) + MODIFIER));
  }

  @Override
  public int getCost() {
    return cost;
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
    return false;
  }

  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getCoins() >= cost;
  }
}
