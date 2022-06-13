package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;

import java.beans.PropertyChangeEvent;

public class MenuEffect extends MenuGame {
  public MenuEffect() {
    super();
  }

  @Override
  protected void showOptions() {
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();

    out.println();
    out.println("Description:");
    out.println(cc.getCardEnum().getDescription());
    out.println("Q - I changed my mind. Undo.");
    out.println("ANY_KEY - Continue");
  }

  @Override
  public MenuEnum show() {
    CharacterCard cc = controller.getGameState().getPlayingField().getPlayedCharacterCard();
    ParamBuilder paramBuilder = new ParamBuilder();

    while (true) {

      // Show CC descriptions
      showOptions();

      // Escape condition
      String choice = getNonBlankString();
      if (choice.equalsIgnoreCase("q")) return null;

      // If the card requires color input
      if (cc instanceof ColorInputCards) {
        // Get color input
        out.println("Insert color: ");
        new MenuStudentColor().show(paramBuilder);
        ((ColorInputCards) cc).setColor(paramBuilder.getChosenColor());
      }

      // If the card requires island index input
      if (cc instanceof IslandInputCards) {
        // View islands
        new IslandsView(islands(), motherPosition()).draw(out);
        // Get island index input
        out.println("Insert island index: ");
        int index = getNumber();
        ((IslandInputCards) cc).setIslandIndex(index);
      }

      // Send the action
      if (controller.sender().sendActivateEffect(cc)) {
        waitForGreenLight();
        return MenuEnum.PLACING;
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
