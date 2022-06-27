package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class AssistCardHandler extends SectionHandler {
  private DebugScreenHandler debugScreenHandler;
  private final TilePane assistCards;
  private final VBox playedCards;

  private final GameState gameState = Controller.get().getGameState();
  private boolean isFirstActionTurn;

  public AssistCardHandler(TilePane assistCards, VBox playedCards, DebugScreenHandler debugScreenHandler) {
    this.assistCards = assistCards;
    this.playedCards = playedCards;
    this.debugScreenHandler = debugScreenHandler;
  }

  @Override
  protected void refresh() {
    GamePhase gamePhase = gameState.getGamePhase();
    TurnPhase turnPhase = gameState.getTurnPhase();
    if (gamePhase == GamePhase.PLANNING) {
      isFirstActionTurn = false;
      createAssistCards();
    }
    if (gamePhase == GamePhase.ACTION && !isFirstActionTurn) {
      isFirstActionTurn = true;
      createAssistCards();
    }
    createPlayedCards();
  }

  @Override
  protected void create() {
    createAssistCards();
    createPlayedCards();
  }

  private void createAssistCards() {
    assistCards.getChildren().clear();
    ArrayList<AssistantCard> cards = gameState.getPlayer(Controller.get().getNickname()).getCards();
    cards.forEach(card -> {
      ImageView img = new ImageView();
      URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.value + ".png");
      img.setImage(new Image(String.valueOf(imgPath)));
      img.setFitWidth(160);
      img.setId(card.toString());
      img.setPreserveRatio(true);
      //gray out a card if it has already been played by others
      if (!isCardPlayable(card)){
        ColorAdjust filter = new ColorAdjust();
        filter.setSaturation(-1);
        Blend blush = new Blend(BlendMode.MULTIPLY, filter,
                new ColorInput(
                        0,
                        0,
                        img.getImage().getRequestedWidth(),
                        img.getImage().getRequestedHeight(),
                        Color.GRAY
                )
        );
        img.setEffect(blush);
      }
      if (gameState.getGamePhase() == GamePhase.PLANNING) {
        img.setCursor(Cursor.HAND);
        img.setOnMouseClicked(e -> playAssistCardAction(card));
      }
      assistCards.getChildren().add(img);
    });
  }

  private void createPlayedCards() {
    playedCards.getChildren().clear();
    Text text = new Text("Played cards:");
    text.getStyleClass().add("text-playedcard");
    playedCards.getChildren().add(text);
    gameState.getPlayers().forEach(player -> {
      Label chosenCard = new Label();
      Optional<AssistantCard> card = player.getChosenCard();
      if (card.isPresent()) {
        chosenCard.setText(player.getNickname());
        URL imgPath = getClass().getResource("/assets/assistcards/Animali_1_" + card.get().value + ".png");
        ImageView cardIcon = new ImageView(new Image(String.valueOf(imgPath)));
        cardIcon.setFitWidth(60);
        cardIcon.setPreserveRatio(true);
        chosenCard.setGraphic(cardIcon);
        chosenCard.setPickOnBounds(false);
        chosenCard.getStyleClass().add("text-playedcard");
      }
      playedCards.getChildren().add(chosenCard);
    });
  }

  private void refreshPlayedCards() {

  }

  private void playAssistCardAction(AssistantCard card) {
    int index = gameState
            .getPlayer(Controller.get().getNickname()).getCards().indexOf(card);
    if (!Controller.get().sender().sendPickAssistantCard(index))
      debugScreenHandler.showMessage("error assist card action invalid");
    else
      debugScreenHandler.showMessage("played " + card.toString());
  }

  private boolean isCardPlayable(AssistantCard card) {
    Player currPlayer = gameState.getCurrentPlayer();
    try {
      // Saves already played cards
      List<AssistantCard> alreadyPlayedCards = new ArrayList<>();
      gameState.getPlayers().forEach(player -> {
        if (player.getChosenCard().isPresent()) {
          alreadyPlayedCards.add(player.getChosenCard().get());
        }
      });

      // Checks if the card with the same value has been already played
      for (AssistantCard c : alreadyPlayedCards) {
        if (card.equals(c)) {
          // If so, if I have more than one other playable card it's not a valid choice
          for (AssistantCard myCard : currPlayer.getCards()) {
            if (!alreadyPlayedCards.contains(myCard)) {
              return false;
            }
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      modelLogger.warn("\nIndex out of bound for Assistant card list. Message: " + e.getMessage());
      return false;
    }
    return true;
  }
}
