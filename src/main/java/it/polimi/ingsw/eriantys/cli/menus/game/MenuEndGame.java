package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.cli.views.View;
import it.polimi.ingsw.eriantys.cli.views.ViewGroup;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;
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
    out.println("Game ended");
    View endScreen = new ViewGroup()
        .addView(islandsView())
        .addView(dashboardsView())
        .addView(playersView());

    endScreen.draw(out);

    StringBuilder result = new StringBuilder();
    game().getWinner().ifPresentOrElse(
        winner -> result
            .append(colored("TEAM ", GREEN))
            .append(colored(winner.toString(), winner))
            .append(" HAS WON THE GAME"),
        () ->
            result.append("IT'S A TIE")
    );

    out.print(result);

    return MenuEnum.CREATE_OR_JOIN;
  }
}
