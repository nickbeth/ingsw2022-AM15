package it.polimi.ingsw.eriantys.gui.controllers.utils;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;

import java.util.EnumMap;

public class ImagePaths {
  public static final EnumMap<TowerColor, String> towerColorToPath = new EnumMap<>(TowerColor.class) {{
    for (TowerColor color : TowerColor.values())
      put(color, "/assets/realm/tower-" + color + ".png");
  }};

  public static final EnumMap<HouseColor, String> studentColorToPath = new EnumMap<>(HouseColor.class) {{
    for (HouseColor color : HouseColor.values())
      put(color, "/assets/realm/student-" + color + ".png");
  }};

  public static final EnumMap<HouseColor, String> professorColorToPath = new EnumMap<>(HouseColor.class) {{
    for (HouseColor color : HouseColor.values())
      put(color, "/assets/realm/professor-" + color + ".png");
  }};
}
