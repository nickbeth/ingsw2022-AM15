package it.polimi.ingsw.eriantys.cli.menus.action;

import it.polimi.ingsw.eriantys.cli.Menu;
import it.polimi.ingsw.eriantys.cli.menus.ParamBuilder;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.actions.GameAction;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuEffect extends Menu {
  public MenuEffect(Controller controller) {
    this.controller = controller;
  }

  @Override
  protected void showOptions(PrintStream out) {
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();
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
  public void show(Scanner in, PrintStream out) {
    boolean done = false;
    ParamBuilder paramBuilder = new ParamBuilder();
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();

//    if (cc instanceof NoInputCards) continue;
    do {
      if (cc instanceof ColorInputCards) {
        out.println("Insert color: ");
        (new MenuStudentColor()).show(in, out, paramBuilder);
        ((ColorInputCards) cc).setColor(paramBuilder.getChosenColor());
      }

      if (cc instanceof IslandInputCards) {
        // View islands
        (new IslandsView(controller.getGameState().getPlayingField().getIslands(),
                controller.getGameState().getPlayingField().getMotherNaturePosition()))
                .draw(out);
        out.println("Insert island index: ");

        try {
          int index = in.nextInt();
          ((IslandInputCards) cc).setIslandIndex(index);
        } catch (InputMismatchException e) {
          out.println("Input must be a number");
        }
      }

      if (controller.sendActivateEffect(cc))
        done = true;
      else
        out.println("Invalid input parameters");
    } while (!done);
  }

  @Override
  public Menu next() {
    return null;
  }
}
