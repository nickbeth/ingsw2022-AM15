package it.polimi.ingsw.eriantys.model.actions;

import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.entities.PlayingField;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.StudentSlot;
import it.polimi.ingsw.eriantys.model.enums.TurnPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.fields.ColumnMapping;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceStudentsTest {
  @Mock
  GameState game;
  @Mock
  PlayingField playingField;
  @Mock
  Player p;
  @Mock
  Dashboard d;
  @Mock
  Students entrance;
  @Mock
  Students dining;
  PlaceStudents placeStudents;
  List<StudentMovement> moves;

  @Test
  public void applyTest() {
  }

  public void isValidSetup() {
    moves = new ArrayList<>();

    // Set 3 moves: 3xPINK student from: ENTRANCE
    moves.add(new StudentMovement(HouseColor.PINK, StudentSlot.ENTRANCE, StudentSlot.ISLAND, 0));
    moves.add(new StudentMovement(HouseColor.PINK, StudentSlot.ENTRANCE, StudentSlot.ISLAND, 0));
    moves.add(new StudentMovement(HouseColor.PINK, StudentSlot.ENTRANCE, StudentSlot.ISLAND, 0));

    // Set 2 moves: 2xRED student from: DINING
    moves.add(new StudentMovement(HouseColor.RED, StudentSlot.DINIGN, StudentSlot.ISLAND, 10));
    moves.add(new StudentMovement(HouseColor.RED, StudentSlot.DINIGN, StudentSlot.ISLAND, 0));


    moves.forEach((m) -> Logger.debug("{} - {}", m.studentColor(), m.src()));

    placeStudents = new PlaceStudents("nick", moves);
  }

  /**
   * Test not enough students case
   */
  @Test
  public void notEnoughStudents() {
    final int MAX_ISLAND = 12;
    isValidSetup();
    // Stubs simple checks: phase and nickname
    when(game.getCurrentPlayer()).thenReturn(p);
    when(game.getTurnPhase()).thenReturn(TurnPhase.PLACING);
    when(game.getGamePhase()).thenReturn(GamePhase.ACTION);
    when(game.getPlayingField()).thenReturn(playingField);
    when(playingField.getIslandsAmount()).thenReturn(MAX_ISLAND);
    when(p.getNickname()).thenReturn("nick");
    // Stubs students control
    when(p.getDashboard()).thenReturn(d);
    when(d.getEntrance()).thenReturn(entrance);
    when(d.getDiningHall()).thenReturn(dining);

    // Moves are: from ENTRANCE x3 PINK
    // Moves are: from DINING x2 RED
    when(entrance.getCount(any())).thenReturn(10);
    when(dining.getCount(any())).thenReturn(10);

    assertTrue(placeStudents.isValid(game));

    when(entrance.getCount(any())).thenReturn(10);
    when(dining.getCount(any())).thenReturn(0);

    assertFalse(placeStudents.isValid(game), "False because of dining");

    when(entrance.getCount(any())).thenReturn(0);
    when(dining.getCount(any())).thenReturn(10);

    assertFalse(placeStudents.isValid(game), "False because of entrance");

    when(playingField.getIslandsAmount()).thenReturn(10);

    assertFalse(placeStudents.isValid(game), "False out of bound index");
  }
}