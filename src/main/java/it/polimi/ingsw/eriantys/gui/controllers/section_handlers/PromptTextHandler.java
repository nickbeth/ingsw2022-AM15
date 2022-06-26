package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.character_cards.CharacterCard;
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
    GameState gameState = Controller.get().getGameState();
    GamePhase gamePhase = gameState.getGamePhase();
    TurnPhase turnPhase = gameState.getTurnPhase();
    String currentPlayer = gameState.getCurrentPlayer().getNickname();
    CharacterCard playedCard = gameState.getPlayingField().getPlayedCharacterCard();
    String expertAddon = "";
    if (Controller.get().getNickname().equals(currentPlayer))
      currentPlayer = "YOUR";
    else
      currentPlayer += "'S";

    if (Controller.get().getGameInfo().getMode() == GameMode.EXPERT && playedCard == null)
      expertAddon = " OR PLAY A CHARACTER CARD";

    if (gamePhase == GamePhase.PLANNING)
      return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PLAY AN ASSISTANT CARD";
    if (gamePhase == GamePhase.ACTION) {
      if (turnPhase == TurnPhase.PLACING)
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PLACE " + studentsLeftToMove() + " STUDENTS" + expertAddon;
      if (turnPhase == TurnPhase.MOVING) {
        int maxMovement = gameState.getCurrentPlayer().getMaxMovement();
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO MOVE MOTHER NATURE " + "(MAX: " + maxMovement + " MOVES) " + expertAddon;
      }
      if (turnPhase == TurnPhase.PICKING)
        return "IT'S " + currentPlayer.toUpperCase() + " TURN TO PICK A CLOUD";
    }

    return "YOUR PROMPT GOES HERE";
  }

  private int studentsLeftToMove() {
    RuleBook rules = Controller.get().getGameState().getRuleBook();
    Player currentPlayer = Controller.get().getGameState().getCurrentPlayer();
    int myCount = currentPlayer.getDashboard().getEntrance().getCount();
    int finalCount = rules.entranceSize - rules.playableStudentCount;
    return myCount - finalCount;
  }


}
