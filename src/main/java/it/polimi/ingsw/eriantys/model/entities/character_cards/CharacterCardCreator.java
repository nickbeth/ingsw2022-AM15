package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.entities.character_cards.funcional_effects.CardService;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.List;


public class CharacterCardCreator {

  public static CharacterCard create(CharacterCardEnum playedCard) {
    switch (playedCard) {
      case IGNORE_COLOR -> {
        return new ColorInputCards(((gameState, color) -> {
          List<Island> islands = gameState.getPlayingField().getIslands();
          islands.forEach(island -> island.updateInfluences(gameState.getPlayingField().getProfessorHolder()));

          CardService.ignoreColor(islands, color, gameState.getPlayingField().getProfessorHolder().getProfessorOwner(color));
        }), playedCard, HouseColor.RED);
      }
      case IGNORE_TOWERS -> {
        return new NoInputCards(((gameState) -> {
          List<Island> islands = gameState.getPlayingField().getIslands();
          islands.forEach(island -> island.updateInfluences(gameState.getPlayingField().getProfessorHolder()));

          CardService.ignoreTowers(islands);
        }), playedCard);
      }
      case ADD_TO_INFLUENCE -> {
        return new NoInputCards((gameState) -> {
          final int ADD_INFLUENCE = 2;
          TowerColor currTeam = gameState.getCurrentPlayer().getColorTeam();

          CardService.addToInfluence(ADD_INFLUENCE, gameState.getPlayingField().getIslands(), currTeam);
        }, playedCard);
      }
      case DROP_STUDENTS -> {
        return new ColorInputCards(((gameState, color) -> {
          final int DROP_AMOUNT = 3;
          StudentBag studentBag = gameState.getPlayingField().getStudentBag();
          List<Students> diningHalls = new ArrayList<>();
          gameState.getPlayers().forEach( x -> diningHalls.add(x.getDashboard().getDiningHall()));

          CardService.dropStudents(diningHalls, color, DROP_AMOUNT, studentBag);
        }), playedCard, HouseColor.RED);
      }
      case FORCE_MOTHER_NATURE_EFFECTS -> {
        return new IslandInputCards(((gameState, islandIndex) ->
                CardService.forceMotherNatureEffects(islandIndex,
                        gameState.getPlayingField(), gameState.getPlayers())
        ), playedCard, 0);
      }
      case ADD_TO_MOTHER_NATURE_MOVES -> {
        return new NoInputCards((gameState) -> {
          final int ADD_MOVEMENT = 2;
          Player currentPlayer = gameState.getCurrentPlayer();

          CardService.addToMotherNatureMoves(currentPlayer, ADD_MOVEMENT);
        }, playedCard);
      }
      case LOCK_ISLAND -> {
        return new IslandInputCards(((gameState, islandIndex) -> {
          CardService.lockIsland(gameState.getPlayingField().getIsland(islandIndex));
        }), playedCard, 0);
      }
      case STEAL_PROFESSOR -> {
        return new NoInputCards(gameState -> {
          Dashboard currentDashboard = gameState.getCurrentPlayer().getDashboard();
          List<Dashboard> dashboards = new ArrayList<>();
          gameState.getPlayers().forEach( x -> dashboards.add(x.getDashboard()));

          CardService.stealProfessor(currentDashboard, dashboards, gameState.getPlayingField().getProfessorHolder());
        }, playedCard);
      }
      default -> throw new IllegalArgumentException();
    }
  }
}
