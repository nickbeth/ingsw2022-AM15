package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;

public class NoInputCardHandler extends CharacterCardHandler {

  public NoInputCardHandler(StackPane cardPane, CharacterCard card, DebugScreenHandler debug) {
    super(cardPane, card, debug);
  }

  @Override
  protected void refresh() {
    super.refresh();
    GamePhase gamePhase = Controller.get().getGameState().getGamePhase();
    TurnPhase turnPhase = Controller.get().getGameState().getTurnPhase();
    if ( gamePhase == GamePhase.ACTION  && turnPhase != TurnPhase.PICKING)
      cardImg.setOnMouseClicked(e -> playCard());
    else
      cardImg.setOnMouseClicked(null);
  }

  @Override
  protected void create() {
    super.create();

  }

  private void playCard() {
    debug.showMessage("playing card " + card.getCardEnum());
    Controller.get().addListener(this, GAMEDATA_EVENT.tag);
    int cardIndex = Controller.get().getGameState().getPlayingField().getCharacterCards().indexOf(card);
    Controller.get().sender().sendChooseCharacterCard(cardIndex);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    CharacterCard playedCard = Controller.get().getGameState().getPlayingField().getPlayedCharacterCard();
    Controller.get().removeListener(this, GAMEDATA_EVENT.tag);
    Controller.get().sender().sendActivateEffect(playedCard);
  }
}
