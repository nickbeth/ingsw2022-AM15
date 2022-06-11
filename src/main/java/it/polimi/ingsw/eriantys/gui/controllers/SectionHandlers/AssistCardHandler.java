package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ArrayList;

public class AssistCardHandler extends SectionHandler {
  private DebugScreenHandler debugScreenHandler;
  private final TilePane assistCards;

  private final GameState gameState = Controller.get().getGameState();
  private boolean isFirstActionTurn;

  public AssistCardHandler(TilePane assistCards, DebugScreenHandler debugScreenHandler) {
    this.assistCards = assistCards;
    this.debugScreenHandler = debugScreenHandler;
  }

  @Override
  protected void refresh() {
    GamePhase gamePhase = gameState.getGamePhase();
    TurnPhase turnPhase = gameState.getTurnPhase();
    if (gamePhase == GamePhase.PLANNING) {
      isFirstActionTurn = false;
      create();
    }
    if (gamePhase == GamePhase.ACTION && !isFirstActionTurn) {
      isFirstActionTurn = true;
      create();
    }
  }

  @Override
  protected void create() {
    assistCards.getChildren().clear();
    ArrayList<AssistantCard> cards = gameState.getPlayer(Controller.get().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.value + ".png");
      img.setImage(new Image(String.valueOf(imgPath)));
      img.setFitWidth(160);
      img.setId(card.toString());
      img.setPreserveRatio(true);
      if (gameState.getGamePhase() == GamePhase.PLANNING) {
        img.setCursor(Cursor.HAND);
        img.setOnMouseClicked(e -> playAssistCardAction(card));
      }
      assistCards.getChildren().add(img);
    });
  }

  private void playAssistCardAction(AssistantCard card) {
    int index = gameState
            .getPlayer(Controller.get().getNickname()).getCards().indexOf(card);
    if (!Controller.get().sender().sendPickAssistantCard(index))
      debugScreenHandler.showMessage("error assist card action invalid");
    else
      debugScreenHandler.showMessage("played " + card.toString());
  }
}
