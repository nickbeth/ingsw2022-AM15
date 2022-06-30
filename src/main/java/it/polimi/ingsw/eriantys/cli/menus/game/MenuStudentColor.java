package it.polimi.ingsw.eriantys.cli.menus.game;

import it.polimi.ingsw.eriantys.cli.menus.MenuEnum;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.eriantys.model.enums.HouseColor.*;

public class MenuStudentColor extends MenuGame {

  public MenuStudentColor() {
    super();
    showOptions();
    addListeningEvents();
  }

  private void addListeningEvents() {
    eventsToBeListening.forEach(eventType -> controller.addListener(this, eventType.tag));
  }

  private void removeListeningEvents() {
    eventsToBeListening.forEach(eventType -> controller.removeListener(this, eventType.tag));
  }

  @Override
  public void showOptions() {
    out.println("Colors:");
    out.println("1 - Green", GREEN);
    out.println("2 - Red", RED);
    out.println("3 - Yellow", YELLOW);
    out.println("4 - Pink", PINK);
    out.println("5 - Blue", BLUE);
    out.print("Choose the color of the students you want to move: ");
  }

  @Override
  public MenuEnum show() throws InterruptedException {
    return null;
  }

  public void show(ParamBuilder paramBuilder) {

    while (true) {
      String choice = getNonBlankString();

      // Choose the color
      switch (choice) {
        case "1", "2", "3", "4", "5" -> {
          paramBuilder.setChosenColor(getHouseColor(choice));
          removeListeningEvents();
          return;
        }
        default -> out.print("Insert a valid option: ");
      }
    }
  }

  private HouseColor getHouseColor(String choice) {
    Map<String, HouseColor> houseColorMap = new HashMap<>();

    houseColorMap.put("1", GREEN);
    houseColorMap.put("2", RED);
    houseColorMap.put("3", YELLOW);
    houseColorMap.put("4", PINK);
    houseColorMap.put("5", BLUE);

    return houseColorMap.get(choice);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
  }
}
