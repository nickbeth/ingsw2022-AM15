package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameState {
  final private List<Player> players = new ArrayList<>(); // Players in the game
  final private List<Player> turnOrder = new ArrayList<>(); // List of players sorted by their turn order
  private int currentPlayer; // Nickname of the current player
  private GamePhase phase; // Current phase of the game
  private TurnPhase turnPhase; // Current turn phase
  private List<TowerColor> teams; // Active tower colors in this game

  private final RuleBook ruleBook; // Set of rules used in this game
  private PlayingField playingField; // Playing field of this game

  public GameState(int playerCount, GameMode mode) {
    ruleBook = RuleBook.makeRules(mode, playerCount);
  }

  public void addPlayer(String playerName, TowerColor towerColor, Students entrance) {
    players.add(new Player(ruleBook, playerName, towerColor, entrance));
  }

  public void advancePlayer() {
    currentPlayer++;
  }

  /**
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return turnOrder.get(currentPlayer);
  }

  /**
   * @return the playing field of this game
   */
  public PlayingField getPlayingField() {
    return playingField;
  }

  /**
   * @return the active tower colors in the game
   */
  public List<TowerColor> getTeams() {
    return teams;
  }

  /**
   * @return the current phase of the game
   */
  public GamePhase getPhase() {
    return phase;
  }

  /**
   * @return the current turn phase
   */
  public TurnPhase getTurnPhase() {
    return turnPhase;
  }

  /**
   * Checks if one of the players has won the game
   */
  public void checkWinCondition() {
    // TODO
  }

  /**
   * Sorts players by their selected assistant card movement value
   */
  private void sortPlayersByTurnPriority() {
    turnOrder.clear();
    turnOrder.addAll(players);
    turnOrder.sort(Comparator.comparingInt(Player::getTurnPriority));
  }
}
