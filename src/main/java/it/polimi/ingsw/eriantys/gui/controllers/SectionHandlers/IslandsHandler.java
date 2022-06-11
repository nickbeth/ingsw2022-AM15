package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Island;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.layout.CircularPane;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

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
   * Updates all island Handlers
   */
  @Override
  protected void refresh() {
    islandsHandlers.forEach(SectionHandler::update);
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
