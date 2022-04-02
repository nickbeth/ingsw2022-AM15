package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsTest {
  @Mock
  Students s;

  @Test
  void addStudents() {
    when(s.getValue(any())).thenReturn(1);
    Students students = new Students();
    students.addStudents(s);
    Arrays.stream(HouseColor.values()).forEach(color ->
            assertEquals(students.getValue(color), 1));
  }

  @Test
  void tryRemoveStudent() {
    when(s.getValue(any())).thenReturn(1);
    HouseColor color = HouseColor.PINK;
    assertFalse(new Students().tryRemoveStudent(color));
    assertTrue(new Students(s).tryRemoveStudent(color));
  }
}