package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;

public class LockIslandCardHandler extends CharacterCardHandler {

  public LockIslandCardHandler(StackPane cardPane, CharacterCard card, DebugScreenHandler debug) {
    super(cardPane, card, debug);
  }

  @Override
  protected void refresh() {
    super.refresh();
  }

  @Override
  protected void create() {
    super.create();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }
}
