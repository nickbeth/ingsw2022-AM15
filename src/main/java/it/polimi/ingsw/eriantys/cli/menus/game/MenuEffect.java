package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;

public class MenuEffect extends MenuGame {
  public MenuEffect() {
    super();
  }

  @Override
  protected void showOptions(PrintStream out) {
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();

    out.println("Description:");
    out.println(cc.getCardEnum().getDescription());
  }

  @Override
  public MenuEnum show(Scanner in, PrintStream out) {
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();
    ParamBuilder paramBuilder = new ParamBuilder();

//    if (cc instanceof NoInputCards) continue;
    while (true) {

      // Show CC descriptions
//      showOptions(out);

      // If the card requires color input
      if (cc instanceof ColorInputCards) {
        // Get color input
        out.println("Insert color: ");
        (new MenuStudentColor()).show(in, out, paramBuilder);
        ((ColorInputCards) cc).setColor(paramBuilder.getChosenColor());
      }

      // If the card requires island index input
      if (cc instanceof IslandInputCards) {
        // View islands
        (new IslandsView(controller.getGameState().getPlayingField().getIslands(),
            controller.getGameState().getPlayingField().getMotherNaturePosition()))
            .draw(out);
        // Get island index input
        out.println("Insert island index: ");
        int index = getNumber(in, out);
        ((IslandInputCards) cc).setIslandIndex(index);
      }

      // Send the action
      if (controller.sender().sendActivateEffect(cc)) {
        waitForGreenLight();
        return null;
      }
      out.println("Invalid input parameters");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
