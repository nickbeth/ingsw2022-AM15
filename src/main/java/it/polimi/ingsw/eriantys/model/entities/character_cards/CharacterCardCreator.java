package it.polimi.ingsw.eriantys.model.entities.character_cards;

import it.polimi.ingsw.eriantys.model.GameService;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.StudentBag;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers.InfluenceModifier;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.List;


public class CharacterCardCreator {
  private GameState gameState;

  public CharacterCardCreator(GameState gameState) {
    this.gameState = gameState;
  }

  public static CharacterCard create(CharacterCardEnum characterCardEnumDrawn) {
    switch (characterCardEnumDrawn) {
      case IGNORE_COLOR -> {
        HouseColor ignoredColor = null;
        return new InfluenceModifier((gameState, cardEffects) -> {
          List<Island> islands = gameState.getPlayingField().getIslands();
          TowerColor teamOwningIgnoredProfessor = gameState.getPlayingField().getProfessorHolder().getProfessorOwner(ignoredColor);
          cardEffects.ignoreColor(islands, ignoredColor, teamOwningIgnoredProfessor);
        }, characterCardEnumDrawn, ignoredColor);
      }
      case IGNORE_TOWERS -> {
        return new InfluenceModifier((gameState, cardEffects) -> {
          List<Island> islands = gameState.getPlayingField().getIslands();
          cardEffects.ignoreTowers(islands);
        }, characterCardEnumDrawn);
      }
      case ADD_TO_INFLUENCE -> {
        return new InfluenceModifier((gameState, cardEffects) -> {
          final int MODIFIER = 2;
          TowerColor currTeam = gameState.getCurrentPlayer().getColorTeam();
          cardEffects.addToInfluence(MODIFIER, gameState.getPlayingField().getIslands(), currTeam);
        }, characterCardEnumDrawn);
      }
      case DROP_STUDENTS -> {
        HouseColor colorToBeDropped = null;
        return new IndependentEffect(gameState -> {
          int DROP_STUDENTS_AMOUNT = 3;
          IGameService gameService = GameService.getGameService();
          List<Students> diningHallList = new ArrayList<>();
          gameState.getPlayers().forEach((player) ->
                  diningHallList.add(player.getDashboard().getDiningHall())
          );
          StudentBag bag = gameState.getPlayingField().getStudentBag();
//          gameService.dropStudents(diningHallList, colorToBeDropped, DROP_STUDENTS_AMOUNT, bag);
        }, characterCardEnumDrawn, colorToBeDropped);
      }
      case FORCE_MOTHER_NATURE_EFFECTS -> {
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
