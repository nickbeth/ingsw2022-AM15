package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.model.entities.enums.HouseColor;

import java.util.Random;

public class StudentBag {
    private Students students;

    public StudentBag() {
        students = new Students();
    }

    /**
     * initializes the student amount in students to a certain amount for every color
     * @param amount
     */
    public void initStudents(int amount) {
        students.setStudents(new Students());
        for (int i = 0; i < amount; i++) {
            students.addStudent(HouseColor.BLUE);
            students.addStudent(HouseColor.RED);
            students.addStudent(HouseColor.GREEN);
            students.addStudent(HouseColor.YELLOW);
        }
    }

    public void addStudent(HouseColor color) {
        students.addStudent(color);
    }

    /**
     * Returns a random student from students
     * @return HouseColor
     */
    public HouseColor takeRandomStudent() {
        HouseColor student;
        Random rand = new Random();
        do {
            student = HouseColor.values()[rand.nextInt(HouseColor.values().length)];
        }
        while (!students.tryRemoveStudent(student));
        return student;
    }

}
