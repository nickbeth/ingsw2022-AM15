package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.CharacterCardsView;
import it.polimi.ingsw.eriantys.cli.views.IslandsView;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.entities.character_cards.IslandInputCards;

import java.beans.PropertyChangeEvent;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.RED;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;

public class MenuEffect extends MenuGame {
  public MenuEffect() {
    super();
  }

  @Override
  protected void showOptions() {
  }

  @Override
  public MenuEnum show() {
    // Choose the cards
    chooseCharacterCard();

    out.println("I'm not stuck in a loop anymore");

    // Show CC descriptions
    CharacterCard cc = game().getPlayingField().getPlayedCharacterCard();
    out.println();
    out.println("Card chosen: " + cc.getCardEnum());
    out.print("Description: ");
    out.println(cc.getCardEnum().getDescription());

    ParamBuilder paramBuilder = new ParamBuilder();

    while (true) {
      // If the card requires color input
      if (cc instanceof ColorInputCards) {
        // Get color input
        out.print("Insert color. ");
        new MenuStudentColor().show(paramBuilder);
        ((ColorInputCards) cc).setColor(paramBuilder.getChosenColor());
      }

      // If the card requires island index input
      if (cc instanceof IslandInputCards) {
        // View islands
        new IslandsView(islands(), motherPosition()).draw(out);
        // Get island index input
        out.print("Insert island index: ");
        int index = getNumber() - 1;  // Index correction
        ((IslandInputCards) cc).setIslandIndex(index);
      }

      // Send the action<
      if (controller.sender().sendActivateEffect(cc)) {
        waitForGreenLight();
        out.println("Card activated. ");
        new CharacterCardsView(List.of(cc)).draw(out);
        if (studentsLeftToMove() == 0)
          return MenuEnum.MOVING;
        else
          return MenuEnum.PLACING;
      }

      // Print reasons why action is invalid
      if (me().getCoins() < cc.getCost())
        out.println(colored("Not enough coins", YELLOW));
      else
        out.println(colored("Invalid input parameters", RED));
    }

  }

  private int studentsLeftToMove() {
    int myCount = me().getDashboard().getEntrance().getCount();
    int finalCount = rules().entranceSize - rules().playableStudentCount;
    return myCount - finalCount;
  }

  private void chooseCharacterCard() {
    // Show playable CC
    List<CharacterCard> playableCC = ccs();
    out.println();
    while (true) {
      new CharacterCardsView(playableCC).draw(out);

      // Choose CC
      out.print("Choose a character card: ");
      int ccIndex = getNumber();

      // Send the action
      if (controller.sender().sendChooseCharacterCard(ccIndex)) {
        waitForGreenLight();
        out.println("Card chosen.");
        break;
      }
      out.println("Choose a valid card");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    greenLight = true;
  }
}
