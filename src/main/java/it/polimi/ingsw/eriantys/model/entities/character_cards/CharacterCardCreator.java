package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.List;

public class CharacterCardCreator {

  public static CharacterCard create(CharacterCardEnum playedCard) {
    switch (playedCard) {
      case IGNORE_COLOR -> {
        HouseColor ignoredColor = null;
//        return new NoInputCC((gameState, cardEffects) -> {
//          List<Island> islands = gameState.getPlayingField().getIslands();
//          TowerColor teamOwningIgnoredProfessor = gameState.getPlayingField().getProfessorHolder().getProfessorOwner(ignoredColor);
//          cardEffects.ignoreColor(islands, ignoredColor, teamOwningIgnoredProfessor);
//        }, characterCardEnumDrawn, ignoredColor);
      }
      case IGNORE_TOWERS -> {
        return new NoInputCC(((gameState) -> {
          List<Island> islands = gameState.getPlayingField().getIslands();
          islands.forEach(island -> island.updateInfluences(gameState.getPlayingField().getProfessorHolder()));
          CardService.ignoreTowers(islands);
        }), playedCard);
      }
      case ADD_TO_INFLUENCE -> {
        return new NoInputCC((gameState) -> {
          final int MODIFIER = 2;
          TowerColor currTeam = gameState.getCurrentPlayer().getColorTeam();
          CardService.addToInfluence(MODIFIER, gameState.getPlayingField().getIslands(), currTeam);
        }, playedCard);
      }
      case DROP_STUDENTS -> {
      }
      case FORCE_MOTHER_NATURE_EFFECTS -> {
        return new IslandInputCC(((gameState, islandIndex) ->
                CardService.forceMotherNatureEffects(islandIndex,
                        gameState.getPlayingField(), gameState.getPlayers())
        ), playedCard, 0);
      }
      case ADD_TO_MOTHER_NATURE_MOVES -> {
      }
      case LOCK_ISLAND -> {

      }
      case STEAL_PROFESSOR -> {

      }
      default -> throw new IllegalArgumentException();
    }
    return null;
  }
}
