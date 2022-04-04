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
    when(s.getValue(any())).thenReturn(1);
    Students students = new Students();
    students.addStudents(s);
    Arrays.stream(HouseColor.values()).forEach(color ->
            assertEquals(students.getValue(color), 1));
  }

  @Test
  public void tryRemoveStudent() {
    when(s.getValue(any())).thenReturn(2);
    Students students = new Students(s);
    students.tryRemoveStudent(HouseColor.PINK);
    students.tryRemoveStudent(HouseColor.PINK);
    students.tryRemoveStudent(HouseColor.PINK);
    assertEquals(0,students.getValue(HouseColor.PINK));
  }

}