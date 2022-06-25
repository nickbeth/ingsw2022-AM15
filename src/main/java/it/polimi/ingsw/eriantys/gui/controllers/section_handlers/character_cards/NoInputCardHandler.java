package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class NoInputCardHandler extends CharacterCardHandler {
  private final StackPane cardsPanel;
  private boolean isActivateEffectSent;

  public NoInputCardHandler(StackPane cardPane, CharacterCard card, ImageView crossImg, StackPane cardsPanel, DebugScreenHandler debug) {
    super(cardPane, card, crossImg, debug);
    this.cardsPanel = cardsPanel;
  }

  /**
   * calls super.refresh() and if needed sends activate effect action
   */
  @Override
  protected void refresh() {
    super.refresh();
    GameState gameState = Controller.get().getGameState();
    CharacterCard playedCard = gameState.getPlayingField().getPlayedCharacterCard();
    if (playedCard == null) {
      isActivateEffectSent = false;
    }
    // if activate effect wasn't already sent and this is the played card
    if (!isActivateEffectSent && playedCard != null && playedCard.getCardEnum() == card.getCardEnum()) {
      //try sending activate effect
      if (Controller.get().sender().sendActivateEffect(playedCard)) {
        cardsPanel.setVisible(false);
        isActivateEffectSent = true;
      } else debug.showMessage("invalid " + playedCard.getCardEnum() + " apply effect");
    }
  }

  @Override
  protected void create() {
    super.create();
  }
}
