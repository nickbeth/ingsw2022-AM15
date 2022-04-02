package it.polimi.ingsw.eriantys.model.entities;

import it.polimi.ingsw.eriantys.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.enums.GameMode;
import it.polimi.ingsw.eriantys.model.entities.enums.TowerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tinylog.Logger;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class PlayingFieldTest {
    PlayingField p;

    @BeforeEach
    public void setUp() {
        p = new PlayingField(RuleBook.makeRules(GameMode.NORMAL, 2));
    }


    @Test
    public void moveMotherNature() {
        p.moveMotherNature(12);
        assertEquals(0, p.getMotherNaturePosition());
        p.moveMotherNature(3);
        assertEquals(3, p.getMotherNaturePosition());
    }

    @Test
    public void refillClouds() {
    }

    @Test
    public void hasProfessor() {
    }

    @Test
    public void setProfessorHolder() {
    }

    @Test
    void mergeIslands() {
        p.getIsland(11).setTowerColor(TowerColor.BLACK);
        p.getIsland(0).setTowerColor(TowerColor.BLACK);
        p.getIsland(1).setTowerColor(TowerColor.BLACK);
        ArrayList<Island> oldIslands = new ArrayList<>();
        for (int i = 0; i < p.getIslandsAmount(); i++)
            oldIslands.add(p.getIsland(i));
        p.mergeIslands(0);

        assertSame(oldIslands.get(10), p.getIsland(9));
        assertSame(oldIslands.get(0), p.getIsland(0));
        assertSame(oldIslands.get(3), p.getIsland(2));

        setUp();
        p.getIsland(2).setTowerColor(TowerColor.BLACK);
        p.getIsland(1).setTowerColor(TowerColor.BLACK);
        p.getIsland(0).setTowerColor(TowerColor.BLACK);
        p.moveMotherNature(1);
        oldIslands.clear();
        for (int i = 0; i < p.getIslandsAmount(); i++)
            oldIslands.add(p.getIsland(i));
        p.mergeIslands(1);

        assertSame(oldIslands.get(1), p.getIsland(0));
        assertSame(oldIslands.get(3), p.getIsland(1));
        assertEquals(0, p.getMotherNaturePosition());

        setUp();
        p.getIsland(2).setTowerColor(TowerColor.BLACK);
        p.getIsland(1).setTowerColor(TowerColor.WHITE);
        p.getIsland(0).setTowerColor(TowerColor.BLACK);
        oldIslands.clear();
        for (int i = 0; i < p.getIslandsAmount(); i++)
            oldIslands.add(p.getIsland(i));
        p.mergeIslands(1);
        for (int i = 0; i < p.getIslandsAmount(); i++)
            assertSame(oldIslands.get(i), p.getIsland(i));
    }
}