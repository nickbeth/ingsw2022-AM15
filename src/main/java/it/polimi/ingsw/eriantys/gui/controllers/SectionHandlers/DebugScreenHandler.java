package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class DebugScreenHandler extends SectionHandler {

  private final VBox debugScreen;
  private static final int MAX_MESSAGE_AMOUNT = 8;
  private int messageAmount = 0;

  public DebugScreenHandler(VBox debugScreen) {
    this.debugScreen = debugScreen;
  }

  public void showMessage(String debugMessage) {
    clientLogger.debug(debugMessage);
    Text debugText = new Text(debugMessage);
    if (messageAmount < MAX_MESSAGE_AMOUNT)
      messageAmount++;
    else
      debugScreen.getChildren().remove(0);
    debugScreen.getChildren().add(debugText);
  }

}
