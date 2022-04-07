package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.PlayerAction;
import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.Optional;

public class MoveMotherNature extends PlayerAction {
  int amount;

  public MoveMotherNature(String nickname, int amount) {
    this.playerNickname = nickname;
    this.amount = amount;
  }

  /**
   * Moves motherNature. <br/>
   * If the destination island is not Locked it sets the tower color to the most influential Team
   * and tries to merge adjacent islands.
   * Modifies players' tower count if necessary. <br/>
   * If necessary it will advance gamePhase and turnPhase.
   *
   * @param gameState
   */
  @Override
  public void apply(GameState gameState) {
    PlayingField playingField = gameState.getPlayingField();
    playingField.moveMotherNature(amount);
    int motherNaturePos = playingField.getMotherNaturePosition();

    if (playingField.getIsland(motherNaturePos).isLocked()) {
      playingField.getIsland(motherNaturePos).setLocked(false);
      //TODO lock returns to the characterCard
    } else {
      Optional<TowerColor> mostInfluentialTeam = playingField.getMostInfluential(motherNaturePos);
      Island currIsland = playingField.getIsland(motherNaturePos);

      if (mostInfluentialTeam.isPresent()) {
        // Set tower color
        TowerColor oldColor = currIsland.getTowerColor();
        currIsland.setTowerColor(mostInfluentialTeam.get());

        // If old color != new color => manage player towers
        if (!oldColor.equals(mostInfluentialTeam.get())) {
          for (Player p : gameState.getPlayers()) {

            // Remove towers from conquerors' dashboard
            if (p.getColorTeam() == mostInfluentialTeam.get()) {
              p.getDashboard().removeTowers(currIsland.getTowerCount());
            }

            // Add towers to conquered dashboard
            if (p.getColorTeam() == oldColor) {
              p.getDashboard().addTowers(currIsland.getTowerCount());
            }
          }
        }
        playingField.mergeIslands(motherNaturePos);
      }
    }

    //checks if the current player is the last in turn order and subsequently changes phases
    Player lastPlayer = gameState.getTurnOrder().get(gameState.getTurnOrder().size() - 1);
    if (gameState.getCurrentPlayer().equals(lastPlayer)) {
      gameState.advanceGamePhase();
    }
    gameState.advanceTurnPhase();
    gameState.advancePlayer();
  }

  /**
   * checks:<br/>
   * - If current player is the player who did the action<br/>
   * - If the gamePhase is ACTION<br/>
   * - If the turnPhase is MOVING<br/>
   * - If the amount of movements is allowed<br/>
   *
   * @param gameState
   * @return boolean
   */
  @Override
  public boolean isValid(GameState gameState) {
    return gameState.getCurrentPlayer().getNickname().equals(playerNickname) &&
            gameState.getGamePhase() == GamePhase.ACTION &&
            gameState.getTurnPhase() == TurnPhase.MOVING &&
            amount > 0 &&
            gameState.getCurrentPlayer().getTurnPriority() >= amount;
  }
}
