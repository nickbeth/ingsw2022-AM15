package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.INTERNAL_SOCKET_ERROR;

public class GameSceneController extends FXMLController {
  @FXML
  private TilePane dashboardTowers;
  @FXML
  private GridPane profTableGrid;
  @FXML
  private GridPane entranceGrid;
  @FXML
  private AnchorPane dashboardClient;
  @FXML
  private GridPane studentHallGrid;
  @FXML
  private VBox cloudBox;
  @FXML
  private GridPane islandsGrid;
  @FXML
  private AnchorPane rootPane;
  @FXML
  private GridPane otherPlayersGrid;
  @FXML
  private TilePane assistCards;
  @FXML
  private StackPane assistCardPanel;
  @FXML
  private Button playCardButton;

  PlayerGridHandler playerGridHandler;
  DashboardHandler mainDashboardHandler;
  CloudsHandler cloudBoxHandler;
  IslandsHandler islandsGridHandler;
  AssistCardHandler assistCardTilesHandler;

  @Override
  public void start() {
    super.start();
    Controller.get().addListener(this, GAMEDATA_EVENT.tag);
    Controller.get().addListener(this, INTERNAL_SOCKET_ERROR.tag);
    mainDashboardHandler = new DashboardHandler(Controller.get().getNickname(), studentHallGrid, entranceGrid, profTableGrid, dashboardTowers);
    playerGridHandler = new PlayerGridHandler(otherPlayersGrid);
    cloudBoxHandler = new CloudsHandler(cloudBox);
    islandsGridHandler = new IslandsHandler(islandsGrid);
    assistCardTilesHandler = new AssistCardHandler(assistCards);
  }

  @Override
  public void finish() {
    super.finish();
    Controller.get().removeListener(this, GAMEDATA_EVENT.tag);
    Controller.get().removeListener(this, INTERNAL_SOCKET_ERROR.tag);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(GAMEDATA_EVENT.tag))
      updateAll();
    else {
      quitGameAction();
      gui.showSocketError();
    }
  }

  @Override
  public void updateAll() {
    super.updateAll();
    playerGridHandler.update();
    mainDashboardHandler.update();
    islandsGridHandler.update();
    cloudBoxHandler.update();
  }

  @FXML
  private void quitGameAction() {
    Controller.get().sender().sendQuitGame();
    gui.setScene(SceneEnum.CREATE_OR_JOIN);
  }

  @FXML
  private void showAssistCards() {
    assistCardTilesHandler.update();
    assistCardPanel.setVisible(true);
    otherPlayersGrid.getClip();
  }

  @FXML
  private void hideAssistCards() {
    assistCardPanel.setVisible(false);
  }

}
