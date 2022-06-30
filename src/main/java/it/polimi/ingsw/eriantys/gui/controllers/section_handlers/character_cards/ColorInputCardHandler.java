package it.polimi.ingsw.eriantys.gui.controllers.section_handlers.character_cards;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.controllers.section_handlers.DebugScreenHandler;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
import it.polimi.ingsw.eriantys.model.entities.character_cards.ColorInputCards;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.eriantys.gui.controllers.utils.ImagePaths.studentColorToPath;

public class ColorInputCardHandler extends CharacterCardHandler {
  private final StackPane cardsPanel;

  private final List<ImageView> studentImgs = new ArrayList<>();

  public ColorInputCardHandler(StackPane cardPane, CharacterCard card, ImageView crossImg, StackPane cardsPanel, DebugScreenHandler debug) {
    super(cardPane, card, crossImg, debug);
    this.cardsPanel = cardsPanel;
  }

  /**
   * - Calls super.refresh()<br>
   * - Refreshes visibility of the student Images.<br>
   */
  @Override
  protected void refresh() {
    super.refresh();
    GameState gameState = Controller.get().getGameState();
    if (!Controller.get().getNickname().equals(gameState.getCurrentPlayer().getNickname())) {
      studentImgs.forEach(img -> img.setVisible(false));
      return;
    }

    CharacterCard playedCard = gameState.getPlayingField().getPlayedCharacterCard();
    if (playedCard != null && playedCard.getCardEnum() == card.getCardEnum())
      studentImgs.forEach(img -> img.setVisible(true));
    else
      studentImgs.forEach(img -> img.setVisible(false));
  }

  @Override
  protected void create() {
    super.create();
    for (HouseColor color : HouseColor.values()) {
      ImageView image = new ImageView(new Image(studentColorToPath.get(color)));
      image.setFitWidth(35);
      image.setPreserveRatio(true);
      image.setVisible(false);
      image.setCursor(Cursor.HAND);
      image.setOnMouseClicked(e -> chooseColor(color));
      studentImgs.add(image);
      cardPane.getChildren().add(image);
    }
    StackPane.setAlignment(studentImgs.get(0), Pos.CENTER_LEFT);
    StackPane.setMargin(studentImgs.get(0), new Insets(0, 0, 0, 4));
    StackPane.setAlignment(studentImgs.get(1), Pos.CENTER_LEFT);
    StackPane.setMargin(studentImgs.get(1), new Insets(0, 0, 0, 43));
    StackPane.setAlignment(studentImgs.get(2), Pos.CENTER_LEFT);
    StackPane.setMargin(studentImgs.get(2), new Insets(0, 0, 0, 83));
    StackPane.setAlignment(studentImgs.get(3), Pos.CENTER_RIGHT);
    StackPane.setMargin(studentImgs.get(3), new Insets(0, 43, 0, 0));
    StackPane.setAlignment(studentImgs.get(4), Pos.CENTER_RIGHT);
    StackPane.setMargin(studentImgs.get(4), new Insets(0, 4, 0, 0));
  }


  private void chooseColor(HouseColor color) {
    ColorInputCards playedCard = (ColorInputCards) Controller.get().getGameState().getPlayingField().getPlayedCharacterCard();
    playedCard.setColor(color);
    if (Controller.get().sender().sendActivateEffect(playedCard)) {
      studentImgs.forEach(img -> img.setVisible(false));
      cardsPanel.setVisible(false);
      cardImg.setCursor(Cursor.HAND);
    } else {
      debug.showMessage("invalid " + card.getCardEnum() + "apply effect");
    }
  }

}
