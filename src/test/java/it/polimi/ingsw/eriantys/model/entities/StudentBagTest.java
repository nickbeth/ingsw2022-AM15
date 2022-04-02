package it.polimi.ingsw.eriantys.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentBagTest {

  @Test
  void takeRandomStudent() {
    StudentBag bag = new StudentBag();
    bag.initStudents(2);
    System.out.println(bag.getStudents());
    for(int i = 0; i < 5; i++)
      System.out.println(bag.takeRandomStudent());
    System.out.println(bag.getStudents());
  }
}