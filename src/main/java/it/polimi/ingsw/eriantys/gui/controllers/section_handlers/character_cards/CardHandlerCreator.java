package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.SectionHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CardHandlerCreator {
  public SectionHandler getCardHandler(CharacterCard card, StackPane cardPane, ImageView crossImg, StackPane cardsPanel, DebugScreenHandler debug) {
    switch (card.getCardEnum().getType()) {
      case ISLAND_INDEX_INPUT -> {
        return new IslandInputCardHandler(cardPane, card, crossImg, cardsPanel, debug);
      }
      case COLOR_INPUT -> {
        return new ColorInputCardHandler(cardPane, card, crossImg, cardsPanel, debug);
      }
      default -> {
        return new NoInputCardHandler(cardPane, card, crossImg, cardsPanel, debug);
      }
    }
  }
}
