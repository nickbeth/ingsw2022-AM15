package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.SectionHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.layout.StackPane;

public class CardHandlerCreator {
  public SectionHandler getCardHandler(CharacterCard card, StackPane cardPane, DebugScreenHandler debug) {
    switch (card.getCardEnum()) {
      case LOCK_ISLAND -> {
        return new LockIslandCardHandler(cardPane, card, debug);
      }
      default -> {
        return new NoInputCardHandler(cardPane, card, debug);
      }
    }
  }
}
