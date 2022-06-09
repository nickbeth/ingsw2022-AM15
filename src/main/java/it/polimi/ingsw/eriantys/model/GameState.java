package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.*;

import java.util.*;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;

public class GameState {
  private List<Player> players = new ArrayList<>(); // Players in the game
  private List<Player> turnOrder = new ArrayList<>(); // List of players sorted by their turn order
  private List<Player> planOrder = new ArrayList<>(); // List of players sorted by their turn order
  private Player currentPlayer;

  private GamePhase gamePhase; // Current phase of the game
  private TurnPhase turnPhase; // Current turn phase

  private RuleBook ruleBook; // Set of rules used in this game
  private PlayingField playingField; // Playing field of this game

  public GameState(int playerCount, GameMode mode) {
    ruleBook = RuleBook.makeRules(mode, playerCount);
    playingField = new PlayingField(ruleBook);
    gamePhase = GamePhase.PLANNING;
    turnPhase = TurnPhase.PLACING;
  }

  /**
   * Adds a player to the player List and planOrder List
   *
   * @param nickname   The player's nickname
   * @param towerColor The chosen tower color
   */
  public void addPlayer(String nickname, TowerColor towerColor) {
    Player newPlayer = new Player(ruleBook, nickname, towerColor, new Students());
    players.add(newPlayer);
    planOrder.add(newPlayer);
    currentPlayer = players.get(0);
    playingField.addTeam(towerColor);
  }

  /**
   * Sets current player to next in line depending on GamePhase
   */
  public void advancePlayer() {
//    currentPlayer = (currentPlayer + 1) % players.size();
    if (getGamePhase() == GamePhase.PLANNING)
      currentPlayer = planOrder.get((planOrder.indexOf(currentPlayer) + 1) % players.size());
    if (getGamePhase() == GamePhase.ACTION)
      currentPlayer = turnOrder.get((planOrder.indexOf(currentPlayer) + 1) % players.size());
  }

  public List<Player> getPlayers() {
    return players;
  }

  /**
   * @return The player corresponding to the given Nickname, or null if none is found
   */
  public Player getPlayer(String nickname) {
    for (Player player : players) {
      if (player.getNickname().equals(nickname))
        return player;
    }
    return null;
  }

  /**
   * returns the current player of current gamePhase
   *
   * @return Player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public List<Dashboard> getDashboards() {
    List<Dashboard> dashes = new ArrayList<>();
    players.forEach(p ->
        dashes.add(p.getDashboard()));
    return dashes;
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
        modelLogger.debug("\nACTION Phase advances to PLANNING");
        gamePhase = GamePhase.PLANNING;
        prepareOrderForNextRound();
      }
      case PLANNING -> {
        modelLogger.debug("\nPLANNING Phase advances to ACTION");
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
//    if (ruleBook.gameMode == GameMode.EXPERT) {
//      switch (turnPhase) {
//        case PLACING -> turnPhase = TurnPhase.EFFECT;
//        case EFFECT -> turnPhase = TurnPhase.MOVING;
//        case MOVING -> turnPhase = TurnPhase.PICKING;
//        case PICKING -> turnPhase = TurnPhase.PLACING;
//      }
//    }
    if (ruleBook.gameMode == GameMode.NORMAL) {
      switch (turnPhase) {
        case PLACING -> turnPhase = TurnPhase.MOVING;
        case MOVING -> turnPhase = TurnPhase.PICKING;
        case PICKING -> turnPhase = TurnPhase.PLACING;
      }
    }
  }

  public void setTurnPhase(TurnPhase turnPhase) {
    this.turnPhase = turnPhase;
  }

  //TODO la win condition deve fare altre cose credo?

  /**
   * Checks:<br>
   * - if there are still students in the student Bag<br>
   * - if every player still has towers<br>
   * - if every player still has assistant cards<br>
   * - if the island amount is allowed<br>
   * - if one of these condition isn't true The Game ends
   */
  public boolean checkWinCondition() {
    if (getPlayingField().getStudentBag().isEmpty()
        || getPlayers().stream().anyMatch(p -> p.getDashboard().noMoreTowers()
        || (p.getCards().size() == 0 && p.getChosenCard().isEmpty()))
        || getPlayingField().getIslandsAmount() <= RuleBook.MIN_ISLAND_COUNT
    ) {
      gamePhase = GamePhase.WIN;
      return true;
    }
    return false;
  }

  /**
   * returns TowerColor of the player who has less towers in their dashboard
   * - if two player have the same amount of towers it checks the amount of professors
   */
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

    for (Player p : getPlayers()) {
      // If the player has fewer towers in his dashboard he's the winner
      if (Temp.getUnusedTowerCount(p) < towerCount) {
        towerCount = Temp.getUnusedTowerCount(p);
        winner = Optional.of(p.getColorTeam());
        heldProfessorCount = Temp.getHeldProfessorCount(p, getPlayingField());
      } else if (Temp.getUnusedTowerCount(p) == towerCount) {
        // If equals number of tower checks held professor count
        if (Temp.getHeldProfessorCount(p, getPlayingField()) > heldProfessorCount) {
          modelLogger.debug("ora vince lui");
          winner = Optional.of(p.getColorTeam());
          heldProfessorCount = Temp.getHeldProfessorCount(p, getPlayingField());
        } else if (Temp.getHeldProfessorCount(p, getPlayingField()) == heldProfessorCount) {
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
   * Sorts turnOrder players by their turn priority (in descending order)
   */
  private void prepareOrderForActionPhase() {
    turnOrder.clear();
    turnOrder.addAll(players);
    modelLogger.debug("\nold turnOrder: " + turnOrder);
    turnOrder.sort(Comparator.comparingInt(Player::getTurnPriority).reversed());
    modelLogger.debug("\nnew turnOrder: " + turnOrder);
  }

  /**
   * Sorts planOrder players clockwise starting from the first of turnOrder
   */
  private void prepareOrderForNextRound() {
    modelLogger.debug("\nold planOrder: " + planOrder);
    planOrder.clear();
    //planOrder.add(turnOrder.get(0));
    int offset = players.indexOf(turnOrder.get(0));
    for (int i = 0; i < players.size(); i++) {
      planOrder.add(players.get((i + offset) % players.size()));
    }
    modelLogger.debug("\nnew planOrder: " + planOrder);
  }

  public boolean isTurnOf(String nickname) {
    return currentPlayer.getNickname().equals(nickname);
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public void setTurnOrder(List<Player> turnOrder) {
    this.turnOrder = turnOrder;
  }

  public void setPlanOrder(List<Player> planOrder) {
    this.planOrder = planOrder;
  }

  public List<Player> getTurnOrderPlayers() {
    return turnOrder;
  }

  public List<Player> getPlanOrderPlayers() {
    return planOrder;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public void setGamePhase(GamePhase gamePhase) {
    this.gamePhase = gamePhase;
  }

  public void setRuleBook(RuleBook ruleBook) {
    this.ruleBook = ruleBook;
  }

  public void setPlayingField(PlayingField playingField) {
    this.playingField = playingField;
  }
}
