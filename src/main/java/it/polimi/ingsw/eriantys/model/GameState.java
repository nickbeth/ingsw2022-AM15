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
  final private List<Player> planOrder = new ArrayList<>(); // List of players sorted by their turn order
  private int currentPlayer; // Nickname of the current player
  private GamePhase phase; // Current phase of the game
  private TurnPhase turnPhase; // Current turn phase
  private final RuleBook ruleBook; // Set of rules used in this game
  private PlayingField playingField; // Playing field of this game
  public GameState(int playerCount, GameMode mode) {
    ruleBook = RuleBook.makeRules(mode, playerCount);
    playingField = new PlayingField(ruleBook);
  }

  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Adds a player to the game
   *
   * @param playerName
   * @param towerColor
   */
  public void addPlayer(String playerName, TowerColor towerColor) {
    Students entrance = new Students();
    for (int i = 0; i <= ruleBook.entranceSize; i++)
      entrance.addStudent(playingField.takeStudentFromBag());
    players.add(new Player(ruleBook, playerName, towerColor, entrance));
    playingField.addTeam(towerColor);
  }

  public void advancePlayer() {
    currentPlayer = (currentPlayer + 1) % players.size();
  }

  /**
   * @return the current player
   */
  public Player getCurrentPlayer() {
    if (getGamePhase() == GamePhase.PLANNING)
      return planOrder.get(currentPlayer);
    if (getGamePhase() == GamePhase.ACTION)
      return turnOrder.get(currentPlayer);
    return null;
  }

  public List<Player> getPlanOrder() {
    return planOrder;
  }

  /**
   * @return the playing field of this game
   */
  public PlayingField getPlayingField() {
    return playingField;
  }

  /**
   * @return the current phase of the game
   */
  public GamePhase getGamePhase() {
    return phase;
  }

  //TODO advanceGamePhase
  public void advanceGamePhase() {
  }

  /**
   * @return the current turn phase
   */
  public TurnPhase getTurnPhase() {
    return turnPhase;
  }

  //TODO advanceTurnPhase
  public void advanceTurnPhase() {
  }

  /**
   * Checks if one of the players has won the game
   */
  public void checkWinCondition() {
    // TODO
  }


  public RuleBook getRuleBook() {
    return ruleBook;
  }

  public List<Player> getTurnOrder() {
    return turnOrder;
  }

  /**
   * Sorts players by their selected assistant card movement value
   */
  private void sortPlayersByTurnPriority() {
    turnOrder.clear();
    turnOrder.addAll(players);
    turnOrder.sort(Comparator.comparingInt(Player::getTurnPriority));
  }

  // todo test
  private void prepareOrderForNextRound() {
    planOrder.clear();
    planOrder.add(turnOrder.get(0));

    int offset = players.indexOf(turnOrder.get(0));
    for (int i = 0; i < players.size(); i++) {
      planOrder.add(players.get((i + offset) % players.size()));
    }
  }

  public boolean isTurnOf(String nickname) {
    return players.get(currentPlayer).getNickname().equals(nickname);
  }
}
