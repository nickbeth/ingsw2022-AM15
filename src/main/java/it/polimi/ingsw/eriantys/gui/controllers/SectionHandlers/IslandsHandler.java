package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Island;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.layout.CircularPane;

import java.util.ArrayList;
import java.util.List;

public class IslandsHandler extends SectionHandler {
  private final DebugScreenHandler debugScreenHandler;
  private final CircularPane islandsCircle;

  private GameState gameState;

  private final List<IslandHandler> islandsHandlers = new ArrayList<>();

  public IslandsHandler(CircularPane islandsCircle, DebugScreenHandler debugScreenHandler) {
    this.islandsCircle = islandsCircle;
    this.debugScreenHandler = debugScreenHandler;
  }

  /**
   * Updates all island Handlers, if an island is flagged as merged it removes it from {@link #islandsCircle}
   */
  @Override
  protected void refresh() {
    List<IslandHandler> toRemove = new ArrayList<>();
    debugScreenHandler.showMessage("refreshing islands");

    islandsHandlers.forEach(handler -> {
      Island island = handler.getIsland();
      if (island.isMerged()) {
        int index = gameState.getPlayingField().getIslands().indexOf(island);
        debugScreenHandler.showMessage("removing island" + index + " from islands circle");
        islandsCircle.remove(handler.getPane());
        toRemove.add(handler);
      } else
        handler.update();
    });
    islandsHandlers.removeAll(toRemove);
  }

  @Override
  protected void create() {
    islandsCircle.getChildren().clear();
    gameState = Controller.get().getGameState();
    List<Island> islands = gameState.getPlayingField().getIslands();
    debugScreenHandler.showMessage("creating island handlers and populating circle");
    islands.forEach(island -> {
      AnchorPane islandPane = new AnchorPane();
      int mnPosition = gameState.getPlayingField().getMotherNaturePosition();
      IslandHandler islandHandler = new IslandHandler(islandPane, island, debugScreenHandler);
      islandHandler.update();
      islandsHandlers.add(islandHandler);
      islandsCircle.add(islandPane);
    });
  }
}
