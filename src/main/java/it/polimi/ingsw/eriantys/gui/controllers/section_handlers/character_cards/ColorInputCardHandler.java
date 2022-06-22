package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;

public class ColorInputCardHandler extends CharacterCardHandler{

  public ColorInputCardHandler(StackPane cardPane, CharacterCard card, DebugScreenHandler debug) {
    super(cardPane, card, debug);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {

  }
}
