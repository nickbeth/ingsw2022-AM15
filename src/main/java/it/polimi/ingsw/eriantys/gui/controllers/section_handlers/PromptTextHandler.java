package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import javafx.scene.text.Text;

public class PromptTextHandler extends SectionHandler {
  private final Text promptText;

  public PromptTextHandler(Text promptText) {
    this.promptText = promptText;
  }

  @Override
  protected void refresh() {
    create();
  }

  @Override
  protected void create() {
    promptText.setText(generatePrompt());
  }

  private String generatePrompt() {
    GamePhase gamePhase = Controller.get().getGameState().getGamePhase();
    TurnPhase turnPhase = Controller.get().getGameState().getTurnPhase();
    String currentPlayer = Controller.get().getGameState().getCurrentPlayer().getNickname();
    String expertAddon = "";
    if (Controller.get().getNickname().equals(currentPlayer))
      currentPlayer = "YOUR";
    else
      currentPlayer += "'S";

    if (Controller.get().getGameInfo().getMode() == GameMode.EXPERT)
      expertAddon = " OR PLAY A CHARACTER CARD";

    if (gamePhase == GamePhase.PLANNING)
      return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PLAY AN ASSISTANT CARD";
    if (gamePhase == GamePhase.ACTION) {
      if (turnPhase == TurnPhase.PLACING)
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PLACE STUDENTS" + expertAddon;
      if (turnPhase == TurnPhase.MOVING)
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO MOVE MOTHER NATURE OR PLAY A CHARACTER CARD" + expertAddon;
      if (turnPhase == TurnPhase.PICKING)
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PICK A CLOUD";
    }

    return "YOUR PROMPT GOES HERE";
  }
}
