package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
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
  public void addStudents() {
    when(s.getCount(any())).thenReturn(1);
    Students students = new Students();
    students.addStudents(s);
    Arrays.stream(HouseColor.values()).forEach(color ->
            assertEquals(students.getCount(color), 1));
  }

  @Test
  public void tryRemoveStudent() {
    when(s.getCount(any())).thenReturn(2);
    Students students = new Students(s);
    students.tryRemoveStudent(HouseColor.PINK);
    students.tryRemoveStudent(HouseColor.PINK);
    students.tryRemoveStudent(HouseColor.PINK);
    assertEquals(0,students.getCount(HouseColor.PINK));
  }

  @Test
  public void getCopy() {
    Students students = new Students();
    students.addStudents(HouseColor.GREEN, 9);
    students.addStudents(HouseColor.RED, 12);
    students.addStudents(HouseColor.YELLOW, 7);
    students.addStudents(HouseColor.PINK, 6);
    students.addStudents(HouseColor.BLUE, 4);

    Students copy = students.getCopy();
    assertTrue(students.containsExactly(copy));
    assertEquals(students.getCount(HouseColor.GREEN), copy.getCount(HouseColor.GREEN));
    assertEquals(students.getCount(HouseColor.RED), copy.getCount(HouseColor.RED));
    assertEquals(students.getCount(HouseColor.YELLOW), copy.getCount(HouseColor.YELLOW));
    assertEquals(students.getCount(HouseColor.PINK), copy.getCount(HouseColor.PINK));
    assertEquals(students.getCount(HouseColor.BLUE), copy.getCount(HouseColor.BLUE));

    students.addStudents(HouseColor.GREEN, 10);
    students.tryRemoveStudent(HouseColor.BLUE);
    students.tryRemoveStudent(HouseColor.YELLOW);
    students.tryRemoveStudent(HouseColor.YELLOW);

    assertEquals(students.getCount(HouseColor.GREEN), copy.getCount(HouseColor.GREEN) + 10);
    assertEquals(students.getCount(HouseColor.RED), copy.getCount(HouseColor.RED));
    assertEquals(students.getCount(HouseColor.YELLOW), copy.getCount(HouseColor.YELLOW) - 2);
    assertEquals(students.getCount(HouseColor.PINK), copy.getCount(HouseColor.PINK));
    assertEquals(students.getCount(HouseColor.BLUE), copy.getCount(HouseColor.BLUE) - 1);

  }
}