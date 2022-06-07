package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class CloudsHandler extends SectionHandler {
  private final VBox cloudBox;

  private final List<CloudHandler> cloudHandlers = new ArrayList<>();

  public CloudsHandler(VBox cloudBox) {
    this.cloudBox = cloudBox;

  }

  /**
   * Updates all cloud Handlers
   */
  @Override
  protected void refresh() {
    //TODO: check in wich gamePhase refillClouds Action gets called
    cloudHandlers.forEach(SectionHandler::update);
  }

  /**
   * Creates the cloud handlers and updates them.
   */
  @Override
  protected void create() {
    clientLogger.debug("creating cloud handlers and populating cloud box");
    Controller.get().getGameState().getPlayingField().getClouds().forEach(
            cloud -> {
              AnchorPane cloudPane = new AnchorPane();
              CloudHandler cloudHandler = new CloudHandler(cloudPane, cloud);
              cloudHandler.update();
              cloudHandlers.add(cloudHandler);
              cloudBox.getChildren().add(cloudPane);
            });
  }

}
