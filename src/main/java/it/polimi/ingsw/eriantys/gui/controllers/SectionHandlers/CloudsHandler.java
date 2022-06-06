package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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
    create();
  }

  /**
   * Creates the cloud handlers and updates them.
   */
  @Override
  protected void create() {
    Controller.get().getGameState().getPlayingField().getClouds().forEach(
            cloud -> {
              AnchorPane cloudPane = new AnchorPane();
              cloudPane.getChildren().add(cloudPane);
              CloudHandler cloudHandler = new CloudHandler(cloudPane, cloud);
              cloudHandler.update();
              cloudHandlers.add(cloudHandler);
            });
  }

}
