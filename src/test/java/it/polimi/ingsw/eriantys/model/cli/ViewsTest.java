package it.polimi.ingsw.eriantys.model.cli;

import it.polimi.ingsw.eriantys.cli.views.DashboardView;
import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.entities.Dashboard;
import it.polimi.ingsw.eriantys.model.entities.ProfessorHolder;
import it.polimi.ingsw.eriantys.model.entities.Students;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewsTest {
  /**
   * Draw a dashboard, play around with it and draw it again.
   */
  @Test
  public void printDashboard() {
    RuleBook ruleBook = RuleBook.makeRules(GameMode.NORMAL, 3);
    Students entrance = new Students();
    entrance.addStudents(HouseColor.RED, 2);
    entrance.addStudents(HouseColor.BLUE, 3);
    entrance.addStudents(HouseColor.YELLOW, 1);
    entrance.addStudents(HouseColor.GREEN, 2);
    entrance.addStudents(HouseColor.PINK, 1);
    assertTrue(entrance.getCount() <= ruleBook.entranceSize);

    Dashboard dashboard = new Dashboard(entrance, ruleBook.dashboardTowerCount, TowerColor.WHITE);

    ProfessorHolder professorHolder = new ProfessorHolder(new EnumMap<>(HouseColor.class));
    DashboardView dashboardView = new DashboardView(ruleBook, dashboard, professorHolder);
    System.out.println("Initial view:");
    dashboardView.draw(System.out);

    // Play around with the dashboard, then draw again
    dashboard.getDiningHall().addStudents(entrance);
    dashboard.getEntrance().tryRemoveStudents(entrance);
    // Only one of the following professors should be printed
    professorHolder.setProfessorHolder(TowerColor.WHITE, HouseColor.BLUE);
    professorHolder.setProfessorHolder(TowerColor.BLACK, HouseColor.YELLOW);
    professorHolder.setProfessorHolder(TowerColor.GRAY, HouseColor.GREEN);
    dashboard.removeTowers(3);
    System.out.println("Dashboard view after some changes:");
    dashboardView.draw(System.out);
  }
}
