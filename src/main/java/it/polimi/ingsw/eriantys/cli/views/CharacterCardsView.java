package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.CustomPrintStream;
import it.polimi.ingsw.eriantys.cli.utils.PrintUtils;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.VERTICAL;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.PADDING;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;
import static it.polimi.ingsw.eriantys.model.enums.HouseColor.YELLOW;


public class CharacterCardsView extends View {
  private final List<CharacterCard> characterCards;


  public CharacterCardsView(List<CharacterCard> characterCards) {
    this.characterCards = characterCards;
  }

  @Override
  public void draw(CustomPrintStream o) {

    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < characterCards.size(); i++) {
      CharacterCard card = characterCards.get(i);
      stringBuilder
          // First row
          .append("╭─╦──COST───╦─EFFECT──────────────────────────╮")
          .append(System.lineSeparator())

          // Content
          .append(PrintUtils.colored(Integer.toString(i), HouseColor.RED))
          .append(PADDING)
          .append(VERTICAL.glyph)
          .append(PADDING)
          .append(PrintUtils.colored(Integer.toString(card.getCardEnum().getCost()), HouseColor.YELLOW))
          .append(PADDING)
          .append(PrintUtils.colored("coins", HouseColor.YELLOW))
          .append(PADDING)
          .append(VERTICAL.glyph)
          .append(PADDING)
          .append(card.getCardEnum().toString());
      int paddingLeft = "EFFECT──────────────────────────".length() - card.getCardEnum().toString().length();
      if (paddingLeft < 0) clientLogger.warn("CharacterCard cli view needs fixes");

      stringBuilder.append(PADDING.repeat(Math.max(0, paddingLeft)));
      stringBuilder
          .append(VERTICAL.glyph)
          .append(System.lineSeparator())
          .append("╰─╩─────────╩─────────────────────────────────╯")
          .append(System.lineSeparator());
    }

    o.println();

    o.println("-----------------CHARACTER CARDS------------------------", YELLOW);

    o.print(stringBuilder);

    o.println("--------------------------------------------------------", YELLOW);
  }

}
