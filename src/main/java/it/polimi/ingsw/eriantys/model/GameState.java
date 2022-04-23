package it.polimi.ingsw.eriantys.model;

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
   * @param playerName Player nickname
   * @param towerColor Team color chosen
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
    class Temp {
      private static int getUnusedTowerCount(Player player) {
        return player.getDashboard().getTowers().count;
      }
      private static int getHeldProfessorCount(Player player, PlayingField playingField) {
        return playingField.getHeldProfessorCount(player.getColorTeam());
      }
    }
    int towerCount = Integer.MAX_VALUE;
    int heldProfessorCount = 0;
    Optional<TowerColor> winner = Optional.empty();

    for (Player p : players) {
      // If the player has fewer towers in his dashboard he's the winner
      if (Temp.getUnusedTowerCount(p) < towerCount) {
        towerCount = Temp.getUnusedTowerCount(p);
        winner = Optional.of(p.getColorTeam());
      } else if (Temp.getUnusedTowerCount(p) == towerCount) {
        // If equals number of tower checks z
        if (Temp.getHeldProfessorCount(p, playingField) > heldProfessorCount) {
          winner = Optional.of(p.getColorTeam());
        } else {
          winner = Optional.empty();
        }
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
    Logger.debug("\nold turnOrder: " + turnOrder);
    turnOrder.sort(Comparator.comparingInt(Player::getTurnPriority).reversed());
    Logger.debug("\nnew turnOrder: " + turnOrder);
  }

  /**
   * Sorts planOrder players clockwise starting from the first of turnOrder
   */
  private void prepareOrderForNextRound() {
    Logger.debug("\nold planOrder: " + planOrder);
    planOrder.clear();
    //planOrder.add(turnOrder.get(0));
    int offset = players.indexOf(turnOrder.get(0));
    for (int i = 0; i < players.size(); i++) {
      planOrder.add(players.get((i + offset) % players.size()));
    }
    Logger.debug("\nnew planOrder: " + planOrder);
  }

  public boolean isTurnOf(String nickname) {
    return players.get(currentPlayer).getNickname().equals(nickname);
  }
}
