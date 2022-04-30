package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinylog.Logger;

import javax.swing.text.html.Option;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PickAssistantCardTest {
  @Mock
  Player curr, p2, p3, p4;
  @Mock
  GameState game;
  @Mock
  ArrayList cards;
  GamePhase phase = GamePhase.PLANNING;
  private String name = "name";
  List<Player> players = new ArrayList<>();

  public void setup() {
    when(game.getCurrentPlayer()).thenReturn(curr);
    //when(game.getGamePhase()).thenReturn(phase);

    players.add(p2);
    players.add(p3);
    players.add(p4);
    players.add(curr);

    when(game.getPlayers()).thenReturn(players);
    //when(curr.getNickname()).thenReturn(name);
  }

  @Test
  void apply() {

  }

  @Test
  public void isValid2() {
    setup();
    Optional<AssistantCard> c1, c2, c3, c4, c5;
    c1 = Optional.of(AssistantCard.ONE);
    c2 = Optional.of(AssistantCard.TWO);
    c3 = Optional.of(AssistantCard.THREE);
    c4 = Optional.of(AssistantCard.FOUR);
    c5 = Optional.of(AssistantCard.FIVE);
    when(p2.getChosenCard()).thenReturn(c2);
    when(p3.getChosenCard()).thenReturn(c3);
    when(p4.getChosenCard()).thenReturn(c4);
    ArrayList<AssistantCard> hand;

    Logger.debug("\nFirst test");
    // CurrPlayer put a different card from others
    hand = new ArrayList<>();
    hand.add(c1.get());
    when(curr.getCards()).thenReturn(hand);

    assertTrue(new PickAssistantCard(0).isValid(game));

    Logger.debug("\nSecond test");

    // CurrPlayer put the same card but had no choice
    hand = new ArrayList<>();
    hand.add(c2.get());
    hand.add(c3.get());
    hand.add(c4.get());
    when(curr.getCards()).thenReturn(hand);

    assertTrue(new PickAssistantCard(0).isValid(game));

    // CurrPlayer had another choice
    hand.add(c5.get());

    assertFalse(new PickAssistantCard(0).isValid(game));
  }
}