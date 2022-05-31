package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ArrayList;

public class AssistCardHandler extends SectionHandler {
  private TilePane assistCards;

  public AssistCardHandler (TilePane assistCards) {
    this.assistCards = assistCards;
  }

  @Override
  protected void refresh() {
    GamePhase gamePhase = Controller.getController().getGameState().getGamePhase();
    if (gamePhase == GamePhase.PLANNING) {
      create();
    }
  }

  @Override
  protected void create() {
    assistCards.getChildren().clear();
    ArrayList<AssistantCard> cards = Controller.getController().getGameState().getPlayer(Controller.getController().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.value + ".png");
      img.setImage(new Image(String.valueOf(imgPath)));
      img.setFitWidth(160);
      img.setId(card.toString());
      img.setPreserveRatio(true);
      if (Controller.getController().getGameState().getGamePhase() == GamePhase.PLANNING) {
        img.setCursor(Cursor.HAND);
        img.setOnMouseClicked(e -> playAssistCardAction(card));
      }
      assistCards.getChildren().add(img);
    });
  }

  private void playAssistCardAction(AssistantCard card) {
    int index = Controller.getController().getGameState()
            .getPlayer(Controller.getController().getNickname()).getCards().indexOf(card);
    Controller.getController().sender().sendPickAssistantCard(index);
  }
}
