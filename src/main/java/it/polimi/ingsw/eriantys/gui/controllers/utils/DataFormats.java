package it.polimi.ingsw.eriantys.gui.controllers.utils;

import javafx.scene.input.DataFormat;

public enum DataFormats {
  HOUSE_COLOR(new DataFormat("eriantys/house-color"));

  public final DataFormat format;

  DataFormats(DataFormat format) {
    this.format = format;
  }
}
