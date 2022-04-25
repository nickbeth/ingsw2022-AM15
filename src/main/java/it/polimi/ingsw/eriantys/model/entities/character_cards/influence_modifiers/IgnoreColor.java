package it.polimi.ingsw.eriantys.model.entities.character_cards.influence_modifiers;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.IGameService;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

public class IgnoreColor implements InfluenceModifierCC, CharacterCard {
  private HouseColor ignoredColor;

  private Island island;
  private ProfessorHolder professorHolder;
  private TowerColor team;

  public IgnoreColor(HouseColor ignoredColor, Island island, ProfessorHolder professorHolder, TowerColor team) {
    this.ignoredColor = ignoredColor;
    this.island = island;
    this.professorHolder = professorHolder;
    this.team = team;
  }

  @Override
  public Integer applyModifier(Integer influence) {
    int modifier = 0;
    // Minus number of student of the ignored color for the team holding the corresponding professor
    for (var color : HouseColor.values())
      if (professorHolder.hasProfessor(color, team) && color == ignoredColor) {
        modifier -= island.getStudents().getCount(color);
      }
    return influence + modifier;
  }

  @Override
  public void applyEffect(GameState gameState, IGameService gameService) {

  }

  @Override
  public boolean requiresInput() {
    return true;
  }

  @Override
  public boolean isValid() {
    return false;
  }
}
