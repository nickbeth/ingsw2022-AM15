package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CloudsHandler extends SectionHandler {
  private final VBox cloudBox;
  private final DebugScreenHandler debugScreenHandler;
  private final List<CloudHandler> cloudHandlers = new ArrayList<>();

  public CloudsHandler(VBox cloudBox, DebugScreenHandler debugScreenHandler) {
    this.cloudBox = cloudBox;
    this.debugScreenHandler = debugScreenHandler;
  }

  /**
   * Updates all cloud Handlers
   */
  @Override
  protected void refresh() {
    //TODO: check in wich gamePhase refillClouds Action gets called
    debugScreenHandler.showMessage("refreshing clouds");
    cloudHandlers.forEach(SectionHandler::update);
  }

  /**
   * Creates the cloud handlers and updates them.
   */
  @Override
  protected void create() {
    debugScreenHandler.showMessage("creating cloud handlers and populating cloud box");
    Controller.get().getGameState().getPlayingField().getClouds().forEach(
            cloud -> {
              AnchorPane cloudPane = new AnchorPane();
              CloudHandler cloudHandler = new CloudHandler(cloudPane, cloud, debugScreenHandler);
              cloudHandler.update();
              cloudHandlers.add(cloudHandler);
              cloudBox.getChildren().add(cloudPane);
            });
  }

}
