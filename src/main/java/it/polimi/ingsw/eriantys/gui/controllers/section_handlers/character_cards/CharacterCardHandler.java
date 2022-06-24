package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.SectionHandler;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.beans.PropertyChangeListener;

public abstract class CharacterCardHandler extends SectionHandler implements PropertyChangeListener {
  protected final StackPane cardPane;
  protected final CharacterCard card;
  private Text addonCoin;
  private ScrollPane descrPane;
  protected ImageView cardImg;

  protected final DebugScreenHandler debug;

  public CharacterCardHandler(StackPane cardPane, CharacterCard card, DebugScreenHandler debug) {
    this.cardPane = cardPane;
    this.card = card;
    this.debug = debug;
  }

  /**
   * Refreshes addon coin visibility and cursor type
   */
  @Override
  protected void refresh() {
    addonCoin.setVisible(card.isUsed());
    GamePhase gamePhase = Controller.get().getGameState().getGamePhase();
    TurnPhase turnPhase = Controller.get().getGameState().getTurnPhase();
    if ( gamePhase == GamePhase.ACTION  && turnPhase != TurnPhase.PICKING)
      cardImg.setCursor(Cursor.HAND);
    else
      cardImg.setCursor(Cursor.DEFAULT);
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
    addonCoin.getStyleClass().add("text-addonCoin");
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
    descrPane.setVisible(!descrPane.isVisible());
  }

}