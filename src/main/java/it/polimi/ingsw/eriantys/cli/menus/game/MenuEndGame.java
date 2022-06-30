package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.View;
import it.polimi.ingsw.eriantys.cli.views.ViewGroup;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.GREEN;

/**
 * End game display
 */
public class MenuEndGame extends MenuGame {
  public MenuEndGame() {
    super();
  }

  @Override
  protected void showOptions() {
  }

  @Override
  public MenuEnum show() {
    out.println();
    out.println(centered("GAME ENDED"), GREEN);
    View endScreen = new ViewGroup()
        .addView(islandsView())
        .addView(dashboardsView())
        .addView(playersView());

    endScreen.draw(out);

    StringBuilder result = new StringBuilder();
    game().getWinner().ifPresentOrElse(
        winner -> result
            .append("TEAM ")
            .append(winner)
            .append(" HAS WON THE GAME"),
        () ->
            result.append("IT'S A TIE")
    );

    out.println("Game ended, see the game above.");
    out.println(result.toString(), GREEN);
    out.println(centered("GAME ENDED"), GREEN);
    out.println();

    out.println("PRESS ANY KEY CONTINUE.");
    getKeyboardInput();

    return MenuEnum.CREATE_OR_JOIN;
  }

  private String centered(String word) {
    StringBuilder baseRow = new StringBuilder(baseSeparator);
    int baseRowLength = baseRow.length();

    int startIndex = (baseRowLength / 2) - (word.length() / 2);

    return baseRow.replace(startIndex, startIndex + word.length(), word).toString();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
  }
}
