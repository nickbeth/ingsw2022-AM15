package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Island;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.*;
import org.tinylog.Logger;

import java.util.*;

public class GameState {
  final private List<Player> players = new ArrayList<>(); // Players in the game
  final private List<Player> turnOrder = new ArrayList<>(); // List of players sorted by their turn order
  final private List<Player> planOrder = new ArrayList<>(); // List of players sorted by their turn order
  private int currentPlayer; // Nickname of the current player
  private GamePhase gamePhase; // Current phase of the game
  private TurnPhase turnPhase; // Current turn phase
  private final RuleBook ruleBook; // Set of rules used in this game
  private PlayingField playingField; // Playing field of this game

  public GameState(int playerCount, GameMode mode) {
    ruleBook = RuleBook.makeRules(mode, playerCount);
    playingField = new PlayingField(ruleBook);
    currentPlayer = 0;
    gamePhase = GamePhase.PLANNING;
    turnPhase = TurnPhase.PLACING;
  }

  /**
   * Adds a player to the player List and planOrder List
   *
   * @param playerName
   * @param towerColor
   */
  public void addPlayer(String playerName, TowerColor towerColor) {
    Player newPlayer = new Player(ruleBook, playerName, towerColor, new Students());
    players.add(newPlayer);
    planOrder.add(newPlayer);
    playingField.addTeam(towerColor);
  }

  /**
   * Advances currentPlayer index
   */
  public void advancePlayer() {
    currentPlayer = (currentPlayer + 1) % players.size();
  }

  public List<Player> getPlayers() {
    return players;
  }

  /**
   * returns the current player of current gamePhase
   *
   * @return Player
   */
  public Player getCurrentPlayer() {
    if (getGamePhase() == GamePhase.PLANNING)
      return planOrder.get(currentPlayer);
    if (getGamePhase() == GamePhase.ACTION)
      return turnOrder.get(currentPlayer);
    return null;
  }

  /**
   * returns order for PLANNING phase
   *
   * @return List of players
   */
  public List<Player> getPlanOrderPlayers() {
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
    return gamePhase;
  }

  /**
   * advances to next gamePhase prepares the corresponding player order
   */
  public void advanceGamePhase() {
    switch (gamePhase) {
      case ACTION -> {
        Logger.debug("\nACTION Phase advances to PLANNING");
        gamePhase = GamePhase.PLANNING;
        prepareOrderForNextRound();
      }
      case PLANNING -> {
        Logger.debug("\nPLANNING Phase advances to ACTION");
        gamePhase = GamePhase.ACTION;
        prepareOrderForActionPhase();
      }
    }
  }

  /**
   * @return the current turn phase
   */
  public TurnPhase getTurnPhase() {
    return turnPhase;
  }

  /**
   * advances to next turnPhase (takes into account gameMode)
   */
  public void advanceTurnPhase() {
    if (ruleBook.gameMode == GameMode.EXPERT) {
      switch (turnPhase) {
        case PLACING -> turnPhase = TurnPhase.EFFECT;
        case EFFECT -> turnPhase = TurnPhase.MOVING;
        case MOVING -> turnPhase = TurnPhase.PICKING;
        case PICKING -> turnPhase = TurnPhase.PLACING;
      }
    }
    if (ruleBook.gameMode == GameMode.NORMAL) {
      switch (turnPhase) {
        case PLACING -> turnPhase = TurnPhase.MOVING;
        case MOVING -> turnPhase = TurnPhase.PICKING;
        case PICKING -> turnPhase = TurnPhase.PLACING;
      }
    }
  }

  //TODO la win condition deve fare altre cose credo?
  // todo e i test non ce li metti?

  /**
   * Checks:<br>
   * - if there are still students in the student Bag<br>
   * - if every player still has towers<br>
   * - if every player still has assistant cards<br>
   * - if the island amount is allowed<br>
   * - if one of these condition isn't true The Game ends
   */
  public void checkWinCondition() {
    if (playingField.getStudentBag().isEmpty()
            || players.stream().allMatch(p -> p.getDashboard().noMoreTowers() || (p.getCards().size() == 0))
            || playingField.getIslandsAmount() <= RuleBook.MIN_ISLAND_COUNT
    ) gamePhase = GamePhase.WIN;
  }

  public Optional<TowerColor> getWinner() {
    Optional<TowerColor> winner = Optional.empty();
    List<Player> players = new ArrayList<>(this.players);
    players.stream().sorted(Comparator.comparingInt(p -> p.getDashboard().getTowers().count));

    Player p1 = players.get(0);
    Player p2 = players.get(1);
    int towerCount1 = playingField.getHeldProfessorCount(p1.getColorTeam());
    int towerCount2 = playingField.getHeldProfessorCount(p2.getColorTeam());

    // Checks who have fewer towers in their dashboard
    if (p1.getDashboard().getTowers().count < p2.getDashboard().getTowers().count) {
      winner = Optional.of(p1.getColorTeam());
    }
    // If they have the same amount of tower
    else if (players.get(0).getDashboard().getTowers().count == players.get(1).getDashboard().getTowers().count) {
      // Checks who have more professor
      if (towerCount1 > towerCount2) {
        winner = Optional.of(p1.getColorTeam());
      } else if (towerCount1 < towerCount2) {
        winner = Optional.of(p2.getColorTeam());
      } else {
        winner = Optional.empty();
      }
    }
    return winner;
  }


  public RuleBook getRuleBook() {
    return ruleBook;
  }

  /**
   * returns player order for ACTION phase
   *
   * @return List of players
   */
  public List<Player> getTurnOrderPlayers() {
    return turnOrder;
  }

  /**
   * Sorts turnOrder players by their turn priority (in descending order)
   */
  private void prepareOrderForActionPhase() {
    turnOrder.clear();
    turnOrder.addAll(players);
    Logger.debug("\nold turnOrder: " + turnOrder.toString());
    turnOrder.sort(Comparator.comparingInt(Player::getTurnPriority).reversed());
    Logger.debug("\nnew turnOrder: " + turnOrder.toString());
  }

  /**
   * Sorts planOrder players clockwise starting from the first of turnOrder
   */
  private void prepareOrderForNextRound() {
    Logger.debug("\nold planOrder: " + planOrder.toString());
    planOrder.clear();
    //planOrder.add(turnOrder.get(0));
    int offset = players.indexOf(turnOrder.get(0));
    for (int i = 0; i < players.size(); i++) {
      planOrder.add(players.get((i + offset) % players.size()));
    }
    Logger.debug("\nnew planOrder: " + planOrder.toString());
  }

  public boolean isTurnOf(String nickname) {
    return players.get(currentPlayer).getNickname().equals(nickname);
  }
}
