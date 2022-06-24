package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class NoInputCardHandler extends CharacterCardHandler {
  private final StackPane cardsPanel;

  public NoInputCardHandler(StackPane cardPane, CharacterCard card, ImageView crossImg, StackPane cardsPanel, DebugScreenHandler debug) {
    super(cardPane, card, crossImg, debug);
    this.cardsPanel = cardsPanel;
  }

  @Override
  protected void refresh() {
    super.refresh();
    GameState gameState = Controller.get().getGameState();
    if (Controller.get().getNickname().equals(gameState.getCurrentPlayer().getNickname())) {
      CharacterCard playedCard = gameState.getPlayingField().getPlayedCharacterCard();
      if (playedCard != null && playedCard.getCardEnum() == card.getCardEnum()) {
        if (Controller.get().sender().sendActivateEffect(playedCard))
          cardsPanel.setVisible(false);
        else debug.showMessage("invalid " + playedCard.getCardEnum() + " apply effect");
      }
    }

  }

  @Override
  protected void create() {
    super.create();
  }
}
