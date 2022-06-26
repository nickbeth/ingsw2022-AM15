package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.SectionHandler;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CharacterCardHandler extends SectionHandler {
  protected final StackPane cardPane;
  protected final CharacterCard card;
  protected final ImageView crossImg;

  private Text addonCoin;
  private ScrollPane descrPane;
  protected ImageView cardImg;

  protected final DebugScreenHandler debug;

  public CharacterCardHandler(StackPane cardPane, CharacterCard card, ImageView crossImg, DebugScreenHandler debug) {
    this.crossImg = crossImg;
    this.cardPane = cardPane;
    this.card = card;
    this.debug = debug;
  }

  /**
   * Refreshes addon coin visibility and card clickability
   */
  @Override
  protected void refresh() {
    crossImg.setVisible(true);
    addonCoin.setVisible(card.isUsed());
    CharacterCard playedCard = Controller.get().getGameState().getPlayingField().getPlayedCharacterCard();
    GameState gameState = Controller.get().getGameState();
    // if the player isn't current the card is not clickable
    if (!Controller.get().getNickname().equals(gameState.getCurrentPlayer().getNickname())) {
      makeNotClickable();
      return;
    }
    if (gameState.getGamePhase() == GamePhase.PLANNING) {
      makeNotClickable();
      return;
    }
    if (playedCard == null) {
      cardImg.setOnMouseClicked(e -> playCard());
      cardImg.setCursor(Cursor.HAND);
    } else {
      makeNotClickable();
    }
  }

  /**
   * Creates base nodes and events needed for all character cards, such as: <br>
   * the card imageView, the description scroll pane and show description button.
   */
  @Override
  protected void create() {
    debug.showMessage("creating " + card.getCardEnum() + " handler");
    cardImg = new ImageView(new Image("/assets/character_cards/cc-" + card.getCardEnum() + ".jpg"));
    cardImg.setPreserveRatio(true);
    cardImg.setFitWidth(200);
    cardPane.getChildren().add(cardImg);

    Button showDescrButton = new Button("show description");
    showDescrButton.setOnAction(e -> toggleDescription());
    cardPane.getChildren().add(showDescrButton);
    StackPane.setAlignment(showDescrButton, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(showDescrButton, new Insets(0, 5, 5, 0));

    addonCoin = new Text("+1");
    addonCoin.getStyleClass().add("label-addoncoin");
    cardPane.getChildren().add(addonCoin);
    StackPane.setAlignment(addonCoin, Pos.TOP_LEFT);
    StackPane.setMargin(addonCoin, new Insets(12, 0, 0, 30));

    descrPane = new ScrollPane();
    descrPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    Text description = new Text(card.getCardEnum().getDescription());
    description.setWrappingWidth(cardImg.getFitWidth());
    descrPane.setContent(description);
    descrPane.setVisible(false);
    cardPane.getChildren().add(descrPane);
    StackPane.setAlignment(descrPane, Pos.CENTER);
    StackPane.setMargin(descrPane, new Insets(120, 0, 50, 0));
  }

  private void toggleDescription() {
    descrPane.toFront();
    descrPane.setVisible(!descrPane.isVisible());
  }

  /**
   * Sends a choose CC message
   */
  protected void playCard() {
    debug.showMessage("playing card " + card.getCardEnum());
    int cardIndex = Controller.get().getGameState().getPlayingField().getCharacterCards().indexOf(card);
    if (!Controller.get().sender().sendChooseCharacterCard(cardIndex))
      debug.showMessage("invalid chosen CC");
  }

  public void makeNotClickable() {
    cardImg.setOnMouseClicked(null);
    cardImg.setCursor(Cursor.DEFAULT);
  }

}
