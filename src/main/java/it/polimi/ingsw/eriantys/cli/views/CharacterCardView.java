package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.VERTICAL;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.PADDING;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.printColored;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;


public class CharacterCardView extends View {
  private final List<CharacterCard> characterCards;


  public CharacterCardView(List<CharacterCard> characterCards) {
    this.characterCards = characterCards;
  }

  @Override
  public void draw(PrintStream o) {
    for (int i = 0; i < characterCards.size(); i++) {
      CharacterCard card = characterCards.get(i);
      o
          .append("╭─╦──COST──╦─EFFECT─────────────╮")
          .append(System.lineSeparator())
          .append(printColored(Integer.toString(i), HouseColor.RED))
          .append(PADDING)
          .append(VERTICAL.glyph)
          .append(printColored(Integer.toString(card.getCardEnum().getCost()), HouseColor.YELLOW))
          .append(PADDING)
          .append(printColored("coins", HouseColor.YELLOW))
          .append(PADDING)
          .append(VERTICAL.glyph)
          .append(PADDING)
          .append(card.getCardEnum().toString());
      int paddingLeft = "EFFECT─────────────".length() - card.getCardEnum().toString().length();
      if (paddingLeft < 0) clientLogger.warn("CharacterCard cli view needs fixes");

      for (int j = 0; j < paddingLeft; j++) {
        o.append(PADDING);
      }
      o
          .append(VERTICAL.glyph)
          .append(System.lineSeparator())
          .append("╰─╩────────╩────────────────────╯")
          .append(System.lineSeparator());
    }
  }

}
