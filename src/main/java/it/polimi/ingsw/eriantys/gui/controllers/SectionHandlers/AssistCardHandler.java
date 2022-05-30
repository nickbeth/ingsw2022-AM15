package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import org.tinylog.Logger;

import java.net.URL;
import java.util.ArrayList;

public class AssistCardHandler extends SectionHandler {
  private TilePane assistCards;

  public AssistCardHandler (TilePane assistCards) {
    this.assistCards = assistCards;
  }

  @Override
  protected void refresh() {
    super.refresh();
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
      img.setCursor(Cursor.HAND);
      img.setOnMouseClicked(this::playAssistCardAction);
      assistCards.getChildren().add(img);
    });
  }

  private void playAssistCardAction(MouseEvent mouseEvent) {
    ImageView img = (ImageView) mouseEvent.getSource();
    Logger.debug("Played assistant card " + img.getId());
  }
}
