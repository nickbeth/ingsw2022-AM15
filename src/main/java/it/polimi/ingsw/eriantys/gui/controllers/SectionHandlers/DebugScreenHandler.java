package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class DebugScreenHandler extends SectionHandler {
  private Text currentPlayer;
  private Text gamePhase;
  private final VBox debugScreen;
  private static final int MAX_MESSAGE_AMOUNT = 8;
  // Amount of messages that don't have to be cycled out
  private static final int FIXED_MESSAGES_AMOUNT = 3;
  private int messageAmount = 0;

  public DebugScreenHandler(VBox debugScreen) {
    this.debugScreen = debugScreen;
    update();
  }

  @Override
  protected void refresh() {
    updateFixedDebugTexts();
  }

  @Override
  protected void create() {
    createFixedDebugTexts();
  }

  public void showMessage(String debugMessage) {
    clientLogger.debug(debugMessage);
    Text debugText = new Text(debugMessage);
    debugText.getStyleClass().add("text-debug");
    if (messageAmount < MAX_MESSAGE_AMOUNT)
      messageAmount++;
    else
      debugScreen.getChildren().remove(FIXED_MESSAGES_AMOUNT);
    debugScreen.getChildren().add(debugText);
  }

  private void createFixedDebugTexts() {
    gamePhase = new Text();
    gamePhase.getStyleClass().add("text-debug");
    currentPlayer = new Text();
    currentPlayer.getStyleClass().add("text-debug");
    debugScreen.getChildren().add(gamePhase);
    debugScreen.getChildren().add(currentPlayer);
    debugScreen.getChildren().add(new Text());
  }

  private void updateFixedDebugTexts() {
    String phaseText = "Phase: " + Controller.get().getGameState().getGamePhase();
    if (Controller.get().getGameState().getGamePhase() == GamePhase.ACTION)
      phaseText += " -> " + Controller.get().getGameState().getTurnPhase();
    gamePhase.setText(phaseText);
    currentPlayer.setText("Turn of: " + Controller.get().getGameState().getCurrentPlayer());
  }

}
