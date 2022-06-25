package it.polimi.ingsw.eriantys.model;

import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.loggers.Loggers.modelLogger;
import static it.polimi.ingsw.eriantys.model.enums.GamePhase.ACTION;
import static it.polimi.ingsw.eriantys.model.enums.GamePhase.PLANNING;

public class GameState implements Serializable {
  private List<Player> players = new ArrayList<>(); // Players in the game
  private List<Player> actionPhaseOrder = new ArrayList<>(); // List of players sorted by their turn order
  private List<Player> planningPhaseOrder = new ArrayList<>(); // List of players sorted by their turn order

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
    planningPhaseOrder.add(newPlayer);
    currentPlayer = players.get(0);
    playingField.addTeam(towerColor);
  }

  /**
   * Sets current player to next in line depending on GamePhase, if the new current player is marked as disconnected
   * it advances player again.
   */
  public void advancePlayer() {
    if (getGamePhase() == GamePhase.PLANNING)
      currentPlayer = planningPhaseOrder.get((planningPhaseOrder.indexOf(currentPlayer) + 1) % players.size());
    if (getGamePhase() == ACTION)
      currentPlayer = actionPhaseOrder.get((actionPhaseOrder.indexOf(currentPlayer) + 1) % players.size());

    //if the new current player is disconnected advances to next player
    if (!currentPlayer.isConnected()) {
      advancePlayer();
    }
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
        modelLogger.debug("ACTION Phase advances to PLANNING");
        gamePhase = GamePhase.PLANNING;
        prepareOrderForPlanningPhase();
        currentPlayer = planningPhaseOrder.get(0);
        if (!currentPlayer.isConnected())
          advancePlayer();
      }
      case PLANNING -> {
        modelLogger.debug("PLANNING Phase advances to ACTION");
        gamePhase = ACTION;
        prepareOrderForActionPhase();
        currentPlayer = actionPhaseOrder.get(0);
        if (!currentPlayer.isConnected())
          advancePlayer();
      }
    }
  }

  public void advance() {
    boolean isLastTurnPhase = turnPhase.equals(TurnPhase.PICKING);
    boolean wasPlanning = gamePhase.equals(GamePhase.PLANNING);
    boolean wasAction = gamePhase.equals(ACTION);
    boolean wasCurrentPlayerConnected = currentPlayer.isConnected();

    int currentPlayerPlanningIndex = planningPhaseOrder.indexOf(currentPlayer);
    int currentPlayerActionIndex = actionPhaseOrder.indexOf(currentPlayer);

    boolean isCurrentPlayerLast =
        wasAction ?
            currentPlayerActionIndex == (actionPhaseOrder.size() - 1) :
            currentPlayerPlanningIndex == (planningPhaseOrder.size() - 1);

    // planning phase advances the same if the current player is connected or not
    if (wasPlanning) {
      // If it was the last player
      if (isCurrentPlayerLast) {
        simpleAdvanceGamePhase();
        prepareOrderForActionPhase();
        currentPlayer = firstOfPhase(ACTION);
        return;
      }

      // if there are no remaining CONNECTED players after the current one
      if (!advancePlanningPlayer()) {
        simpleAdvanceGamePhase();
        prepareOrderForActionPhase();
        currentPlayer = firstOfPhase(ACTION);
      }
      return;
    }

    // planning phase advances the same if the current player is connected or not
    if (wasAction) {
      if (isLastTurnPhase) {
        // If is last player prepare next phase.
        if (isCurrentPlayerLast) {
          simpleAdvanceGamePhase();
          prepareOrderForPlanningPhase();
          currentPlayer = firstOfPhase(PLANNING);
        } else if (!advanceActionPlayer()) {
          simpleAdvanceGamePhase();
          prepareOrderForPlanningPhase();
          currentPlayer = firstOfPhase(PLANNING);
        }
      }

      // if the player was connected advance turn phase
      if (wasCurrentPlayerConnected) {
        simpleAdvanceTurnPhase();
        return;
      }
      // if the player isn't connected, prepare state for the next player
      turnPhase = TurnPhase.PLACING;
      if (!isLastTurnPhase) {
        if (isCurrentPlayerLast) {
          prepareOrderForPlanningPhase();
          currentPlayer = firstOfPhase(PLANNING);
          return;
        }
        if (!advanceActionPlayer()) {
          prepareOrderForPlanningPhase();
          currentPlayer = firstOfPhase(PLANNING);
          return;
        }
        return;
      }
    }

    // originale
//    if (currentPlayer.isConnected()) {
//      switch (gamePhase) {
//        case ACTION -> {
//          simpleAdvanceTurnPhase();
//          if (isLastTurnPhase) {
//            if (isCurrentPlayerLast) {
//              prepareOrderForPlanningPhase();
//              currentPlayer = firstOfPlanning();
//              return;
//            }
//            if (!advanceActionPlayer()) {
//              prepareOrderForPlanningPhase();
//              currentPlayer = firstOfPlanning();
//              return;
//            }
//          }
//        }
//      }
//    } else {
//      // Case current player not connected
//      switch (gamePhase) {
//        case ACTION -> {
//          turnPhase = TurnPhase.PLACING;
//          if (isLastTurnPhase) {
//            if (isCurrentPlayerLast) {
//              prepareOrderForPlanningPhase();
//              currentPlayer = firstOfPlanning();
//            } else {
//              advanceActionPlayer();
//              return;
//            }
//          } else {
//            if (isCurrentPlayerLast) {
//              prepareOrderForPlanningPhase();
//              currentPlayer = firstOfPlanning();
//              return;
//            }
//            advanceActionPlayer();
//            return;
//          }
//        }
//      }
//    }
//
//    if (!currentPlayer.isConnected()) {
//      if (isPlanning) {
//        if (isCurrentPlayerLast) {
//          simpleAdvanceGamePhase();
//          prepareOrderForActionPhase();
//          currentPlayer = firstOfAction();
//        } else {
//          advancePlanningPlayer();
//        }
//        return;
//      }
//      if (isActionPhase) {
//        turnPhase = TurnPhase.PLACING;
//        if (isLastTurnPhase) {
//          if (isCurrentPlayerLast) {
//            simpleAdvanceGamePhase();
//            prepareOrderForPlanningPhase();
//            currentPlayer = firstOfPlanning();
//          } else {
//            advanceActionPlayer();
//            return;
//          }
//        } else {
//          if (isCurrentPlayerLast) {
//            simpleAdvanceGamePhase();
//            prepareOrderForPlanningPhase();
//            currentPlayer = firstOfPlanning();
//            return;
//          } else {
//            advanceActionPlayer();
//            return;
//          }
//        }
//      }
//    }
  }

  /**
   * @return The first CONNECTED player that should play in the given GamePhase
   */
  private Player firstOfPhase(GamePhase phase) {
    switch (phase) {
      case PLANNING -> {
        int i = 0;

        while (!planningPhaseOrder.get(i).isConnected()) {
          i++;
        }
        return planningPhaseOrder.get(i);
      }
      case ACTION -> {
        int i = 0;

        while (!actionPhaseOrder.get(i).isConnected()) {
          i++;
        }
        return actionPhaseOrder.get(i);
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Advance to the first CONNECTED player that should play in PLANNING phase
   *
   * @return False if the remaining player are all disconnected
   */
  private boolean advancePlanningPlayer() {
    int currentPlayerPlanningIndex = planningPhaseOrder.indexOf(currentPlayer);
    try {
      currentPlayer = planningPhaseOrder.get(currentPlayerPlanningIndex + 1);
    } catch (IndexOutOfBoundsException e) {
      // Remaining players are all disconnected
      return false;
    }
    if (!currentPlayer.isConnected())
      return advancePlanningPlayer();
    return true;
  }

  /**
   * Advances to the first CONNECTED player that should play in ACTION phase
   *
   * @return False if the remaining player are all disconnected
   */
  private boolean advanceActionPlayer() {
    int currentPlayerActionIndex = actionPhaseOrder.indexOf(currentPlayer);
    try {
      currentPlayer = actionPhaseOrder.get(currentPlayerActionIndex + 1);
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    if (!currentPlayer.isConnected())
      return advanceActionPlayer();
    return true;
  }

  /**
   * Sorts turnOrder players by their turn priority (in ascending order)
   */
  private void prepareOrderForActionPhase() {
    modelLogger.info("Updated action turn order.");
    modelLogger.debug("Old action order: "
        + actionPhaseOrder.stream().map(player -> player.getNickname() + " " + player.getTurnPriority() + " -> ").toList()
    );
    actionPhaseOrder.clear();
    actionPhaseOrder.addAll(players);
    actionPhaseOrder.sort(Comparator.comparingInt(Player::getTurnPriority));
    modelLogger.debug("New action order: "
        + actionPhaseOrder.stream().map(player -> player.getNickname() + " " + player.getTurnPriority() + " -> ").toList()
    );
  }

  private void simpleAdvanceGamePhase() {
    switch (gamePhase) {
      case ACTION -> gamePhase = GamePhase.PLANNING;
      case PLANNING -> gamePhase = ACTION;
    }
  }

  /**
   * Advances to next turnPhase (takes into account gameMode)
   */
  public void simpleAdvanceTurnPhase() {
    switch (turnPhase) {
      case PLACING -> turnPhase = TurnPhase.MOVING;
      case MOVING -> turnPhase = TurnPhase.PICKING;
      case PICKING -> turnPhase = TurnPhase.PLACING;
    }
  }

  /**
   * @return the current turn phase
   */
  public TurnPhase getTurnPhase() {
    return turnPhase;
  }

  public void advanceIfDisconnected(String disconnectingPlayer) {
    if (currentPlayer.equals(this.getPlayer(disconnectingPlayer))) {
      advancePlayer();
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
   * returns TowerColor of the player who has fewer towers in their dashboard
   * - if two player have the same amount of towers it checks the amount of professors
   */
  public Optional<TowerColor> getWinner() {
    class Tmp {
      private static int getNonUsedTowerCount(Player player) {
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
      if (Tmp.getNonUsedTowerCount(p) < towerCount) {
        towerCount = Tmp.getNonUsedTowerCount(p);
        winner = Optional.of(p.getColorTeam());
        heldProfessorCount = Tmp.getHeldProfessorCount(p, getPlayingField());
      } else if (Tmp.getNonUsedTowerCount(p) == towerCount) {
        // If equals number of tower => checks held professor count
        if (Tmp.getHeldProfessorCount(p, getPlayingField()) > heldProfessorCount) {
          winner = Optional.of(p.getColorTeam());
          heldProfessorCount = Tmp.getHeldProfessorCount(p, getPlayingField());
        } else if (Tmp.getHeldProfessorCount(p, getPlayingField()) == heldProfessorCount) {
          winner = Optional.empty();
        }
      }
    }

    return winner;
  }


  /**
   * Sorts planOrder players clockwise starting from the first of turnOrder
   */
  private void prepareOrderForPlanningPhase() {
    modelLogger.info("Updated planning turn order for the next round.");
    modelLogger.debug("Old planOrder: " + planningPhaseOrder);
    planningPhaseOrder.clear();

    //planOrder.add(turnOrder.get(0));
    // Prepare turns for the next round.
    // First player is the first who played in the previous action phase
    int offset = players.indexOf(actionPhaseOrder.get(0));
    for (int i = 0; i < players.size(); i++) {
      planningPhaseOrder.add(players.get((i + offset) % players.size()));
    }
    modelLogger.debug("New planOrder: " + planningPhaseOrder);
  }

  public RuleBook getRuleBook() {
    return ruleBook;
  }

  /**
   * Returns the last connected player of the current gamePhase
   */
  public boolean isLastPlayer(Player player) {
    switch (gamePhase) {
      case PLANNING -> {
        List<Player> connectedPlayers = planningPhaseOrder.stream().filter(Player::isConnected).toList();
        return player.equals(connectedPlayers.get(connectedPlayers.size() - 1));
      }
      case ACTION -> {
        List<Player> connectedPlayers = actionPhaseOrder.stream().filter(Player::isConnected).toList();
        return player.equals(connectedPlayers.get(connectedPlayers.size() - 1));
      }
      default -> throw new AssertionError();
    }
  }

  /**
   * Overload of method {@link #isLastPlayer(Player)}. <br>
   * Returns the last connected player of the given gamePhase
   *
   * @param player player who's wanted to know
   * @param phase  GamePhase
   */
  public boolean isLastPlayer(Player player, GamePhase phase) {
    switch (phase) {
      case PLANNING -> {
        List<Player> connectedPlayers = planningPhaseOrder.stream().filter(Player::isConnected).toList();
        return player.equals(connectedPlayers.get(connectedPlayers.size() - 1));
      }
      case ACTION -> {
        List<Player> connectedPlayers = actionPhaseOrder.stream().filter(Player::isConnected).toList();
        return player.equals(connectedPlayers.get(connectedPlayers.size() - 1));
      }
      default -> throw new AssertionError();
    }
  }

  public boolean isTurnOf(String nickname) {
    return currentPlayer.getNickname().equals(nickname);
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public void setActionPhaseOrder(List<Player> actionPhaseOrder) {
    this.actionPhaseOrder = actionPhaseOrder;
  }

  public void setPlanningPhaseOrder(List<Player> planningPhaseOrder) {
    this.planningPhaseOrder = planningPhaseOrder;
  }

  public List<Player> getActionPhaseOrder() {
    return actionPhaseOrder;
  }

  public List<Player> getPlanningPhaseOrder() {
    return planningPhaseOrder;
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
