package it.polimi.ingsw.eriantys.view.gui;

import it.polimi.ingsw.eriantys.model.GameState;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

public class IslandsView extends GUIView {

  public IslandsView(GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public void draw() throws IOException {
    gameState.getPlayingField().getIslands().forEach(System.out::print);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      draw();
    } catch (IOException ignored) {}
  }
}
