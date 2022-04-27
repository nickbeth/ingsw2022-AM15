package it.polimi.ingsw.eriantys.controller.menus;

import it.polimi.ingsw.eriantys.controller.io_controls.Input;
import it.polimi.ingsw.eriantys.controller.io_controls.InputImpl;
import it.polimi.ingsw.eriantys.controller.io_controls.Output;
import it.polimi.ingsw.eriantys.controller.io_controls.OutputImpl;
import it.polimi.ingsw.eriantys.model.ActionInvoker;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.ActivateCCEffect;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.OLDCODE.DropStudents;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

public class CCMenu implements Menu {
  @Override
  public void commandMenu(ActionInvoker invoker, String playerNickname, GameState gameState) {
    Input input = new InputImpl();
    Output output = new OutputImpl();

    CharacterCard characterCard = gameState.getPlayingField().getPlayedCharacterCard();

    do {
      if (!gameState.isTurnOf(playerNickname)) {
        output.show("Not your turn");
      } else {
        switch (characterCard.getClass().getSimpleName()) {
          case "LockIsland" -> {
          }
          case "DropStudents" -> {
            invoker.executeAction(new ActivateCCEffect(new DropStudents(HouseColor.PINK)));
          }
          case "ForceMotherNatureEffect" -> {
          }
          case "IgnoreColor" -> {
          }
          case "IgnoreTowers" -> {

          }
          default -> System.out.println();
        }
      }
    } while (gameState.getTurnPhase() == TurnPhase.EFFECT);
  }
}
