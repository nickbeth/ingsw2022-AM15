package it.polimi.ingsw.eriantys.gui.controllers.utils;

import javafx.scene.input.DataFormat;

public enum DataFormats {
  HOUSE_COLOR(new DataFormat("house-color")),
  MOTHER_NATURE(new DataFormat("move-mother-nature")),
  CARD_TO_ISLAND(new DataFormat("card-to-island"));

  public final DataFormat format;

  DataFormats(DataFormat format) {
    this.format = format;
  }
}
