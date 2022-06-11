package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers.*;
import it.polimi.ingsw.eriantys.model.enums.GamePhase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import jfxtras.scene.layout.CircularPane;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.INTERNAL_SOCKET_ERROR;

public class GameSceneController extends FXMLController {
  @FXML
  private Label phase;
  @FXML
  private Label currPlayerLabel;
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
  private CircularPane islandsPane;
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
  IslandsHandler islandsPaneHandler;
  AssistCardHandler assistCardTilesHandler;

  @Override
  public void start() {
    super.start();
    Controller.get().addListener(this, GAMEDATA_EVENT.tag);
    Controller.get().addListener(this, INTERNAL_SOCKET_ERROR.tag);
    mainDashboardHandler = new DashboardHandler(Controller.get().getNickname(), studentHallGrid, entranceGrid, profTableGrid, dashboardTowers);
    playerGridHandler = new PlayerGridHandler(otherPlayersGrid);
    cloudBoxHandler = new CloudsHandler(cloudBox);
    islandsPaneHandler = new IslandsHandler(islandsPane);
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
    if (evt.getPropertyName().equals(GAMEDATA_EVENT.tag))
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
    islandsPaneHandler.update();
    cloudBoxHandler.update();
    updateGameLables();
    if (assistCardPanel.isVisible())
      assistCardTilesHandler.update();
  }

  private void updateGameLables() {
    String phaseText = "Phase: " + Controller.get().getGameState().getGamePhase();
    if (Controller.get().getGameState().getGamePhase() == GamePhase.ACTION)
      phaseText += " -> " + Controller.get().getGameState().getTurnPhase();
    phase.setText(phaseText);
    currPlayerLabel.setText("Turn of: " + Controller.get().getGameState().getCurrentPlayer());
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
