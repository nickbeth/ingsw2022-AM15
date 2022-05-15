package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.entities.Player;

import java.io.PrintStream;

public class AssistantCardsView extends View {
  private Player player;

  public AssistantCardsView(Player player) {
    this.player = player;
  }

  @Override
  public void draw(PrintStream o) {
    for (int i = 0; i < player.getCards().size(); i++) {
      System.out.println("1 - " + player.getCards().get(i));
    }
  }
}
