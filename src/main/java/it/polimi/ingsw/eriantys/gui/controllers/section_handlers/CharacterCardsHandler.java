package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards.CardHandlerCreator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardsHandler extends SectionHandler{
  private final StackPane characterCardsPanel;
  private final TilePane characterCards;

  private final DebugScreenHandler debug;
  private List<SectionHandler> cardHandlers = new ArrayList<>();


  public CharacterCardsHandler (StackPane characterCardsPanel, TilePane characterCards, DebugScreenHandler debug) {
    this.characterCardsPanel = characterCardsPanel;
    this.characterCards = characterCards;
    this.debug = debug;
  }

  @Override
  protected void refresh() {
    cardHandlers.forEach(SectionHandler::update);
    super.refresh();
  }

  @Override
  protected void create() {
    CardHandlerCreator creator = new CardHandlerCreator();
    String debugMessage = "creating CC handlers";
    Controller.get().getGameState().getPlayingField().getCharacterCards().forEach( card -> {
      StackPane cardPane = new StackPane();
      SectionHandler cardHandler = creator.getCardHandler(card, cardPane, debug);
      cardHandler.update();
      cardHandlers.add(cardHandler);
      characterCards.getChildren().add(cardPane);
    });
  }
}
