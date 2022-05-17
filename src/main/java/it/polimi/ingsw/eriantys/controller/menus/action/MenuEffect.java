package it.polimi.ingsw.eriantys.controller.menus.action;

import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.controller.menus.Menu;
import it.polimi.ingsw.eriantys.controller.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuEffect extends Menu {

  public MenuEffect(GameState game, String playerNickname, Controller controller) {
    this.game = game;
    this.playerNickname = playerNickname;
    this.controller = controller;
  }

  @Override
  public void showOptions() {
    CharacterCard cc = game.getPlayingField().getPlayedCharacterCard();
    GameAction action;

//    if (game.getPlayingField().getPlayedCharacterCard() instanceof NoInputCards) {
//      switch (cc.getCardEnum()) {
//        case IGNORE_COLOR -> {
//          action = new ActivateCCEffect(CharacterCardCreator.create(cc.getCardEnum()));
//        }
//        case IGNORE_TOWERS ->
//        case ADD_TO_INFLUENCE ->
//        case ADD_TO_MOTHER_NATURE_MOVES ->
//        case STEAL_PROFESSOR ->
//      }
//    }
//
//    if (game.getPlayingField().getPlayedCharacterCard() instanceof ColorInputCards) {
//      switch (cc.getCardEnum()) {
//        case IGNORE_COLOR ->
//        case IGNORE_TOWERS ->
//        case ADD_TO_INFLUENCE ->
//        case ADD_TO_MOTHER_NATURE_MOVES ->
//        case STEAL_PROFESSOR ->
//      }
//    }
//
//    if (game.getPlayingField().getPlayedCharacterCard() instanceof IslandInputCards) {
//      switch (cc.getCardEnum()) {
//        case FORCE_MOTHER_NATURE_EFFECTS ->
//      }
//    }

  }

  @Override
  public void makeChoice(ParamBuilder paramBuilder) {
    boolean done = false;
    CharacterCard cc = game.getPlayingField().getPlayedCharacterCard();

//    if (cc instanceof NoInputCards) continue;
    do {
      if (cc instanceof ColorInputCards) {
        System.out.println("Insert color: ");
        (new MenuStudentColor()).makeChoice(paramBuilder);
        ((ColorInputCards) cc).setColor(paramBuilder.getChosenColor());
      }

      if (cc instanceof IslandInputCards) {
        // View islands
        (new IslandsView(game.getPlayingField().getIslands()
                , game.getPlayingField().getMotherNaturePosition()))
                .draw(System.out);
        System.out.println("Insert island index: ");

        try {
          int index = (new Scanner(System.in)).nextInt();
          ((IslandInputCards) cc).setIslandIndex(index);
        } catch (InputMismatchException e) {
          System.out.println("Input must be a number");
        }
      }

      if (controller.sendActivateEffect(cc))
        done = true;
      else
        System.out.println("Invalid input parameters");

    } while (!done);
  }

  @Override
  public Menu nextMenu() {
    return null;
  }
}
