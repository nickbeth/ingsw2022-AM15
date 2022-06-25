package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards.CardHandlerCreator;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;


import java.util.ArrayList;
import java.util.List;

public class CharacterCardsHandler extends SectionHandler {
  private final StackPane characterCardsPanel;
  private final TilePane characterCards;

  private final DebugScreenHandler debug;
  private final List<SectionHandler> cardHandlers = new ArrayList<>();


  public CharacterCardsHandler(StackPane characterCardsPanel, TilePane characterCards, DebugScreenHandler debug) {
    this.characterCardsPanel = characterCardsPanel;
    this.characterCards = characterCards;
    this.debug = debug;
  }

  @Override
  protected void refresh() {
    debug.showMessage("refreshing character card handlers");
    cardHandlers.forEach(SectionHandler::update);
  }

  @Override
  protected void create() {
    ImageView closeButton = new ImageView(new Image("/assets/misc/cross-icon.png"));
    closeButton.setFitWidth(27);
    closeButton.setPreserveRatio(true);
    closeButton.setCursor(Cursor.HAND);
    StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    closeButton.setOnMouseClicked(this::hideCharacterCards);
    characterCardsPanel.getChildren().add(closeButton);

    CardHandlerCreator creator = new CardHandlerCreator();
    Controller.get().getGameState().getPlayingField().getCharacterCards().forEach(card -> {
      StackPane cardPane = new StackPane();
      SectionHandler cardHandler = creator.getCardHandler(card, cardPane, closeButton, characterCardsPanel, debug);
      cardHandler.update();
      cardHandlers.add(cardHandler);
      characterCards.getChildren().add(cardPane);
    });
  }

  private void hideCharacterCards(MouseEvent mouseEvent) {
    debug.showMessage("clicked on red cross");
    characterCardsPanel.setVisible(false);
  }

}
