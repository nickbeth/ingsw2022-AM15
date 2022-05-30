package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers.*;
import it.polimi.ingsw.eriantys.model.entities.*;
import it.polimi.ingsw.eriantys.model.enums.AssistantCard;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.tinylog.Logger;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;

public class PlanningSceneController extends FXMLController {
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
  private StackPane playerTwo;
  @FXML
  private StackPane playerFour;
  @FXML
  private StackPane playerThree;
  @FXML
  private TilePane assistCards;
  @FXML
  private StackPane assistCardPanel;
  @FXML
  private Button playCardButton;

  private final AnchorPane[][] islands = new AnchorPane[5][4];
  PlayerGridHandler playerGridHandler;
  DashboardHandler mainDashboardHandler;
  CloudsHandler cloudBoxHandler;
  IslandsHandler islandsGridHandler;
  AssistCardHandler assistCardTilesHandler;

  @Override
  public void start() {
    super.start();
    Controller.getController().addListener(this, GAMEDATA_EVENT.tag);
    mainDashboardHandler = new DashboardHandler(studentHallGrid, entranceGrid, profTableGrid, dashboardTowers);
    playerGridHandler = new PlayerGridHandler(otherPlayersGrid);
    cloudBoxHandler = new CloudsHandler(cloudBox);
    islandsGridHandler = new IslandsHandler(islandsGrid);
    assistCardTilesHandler = new AssistCardHandler(assistCards);
  }

  @FXML
  private void showAssistCards(ActionEvent actionEvent) {
    assistCardTilesHandler.update();
    assistCardPanel.setVisible(true);
    otherPlayersGrid.getClip();
  }

  @FXML
  private void hideAssistCards(MouseEvent mouseEvent) {
    assistCardPanel.setVisible(false);
  }

  @FXML
  private void selDashboardTwo(MouseEvent mouseEvent) {
    Dashboard dashboard = Controller.getController().getGameState().getPlayers().get(1).getDashboard();
  }

  @FXML
  private void selDashboardThree(MouseEvent mouseEvent) {
    ImageView source = (ImageView) mouseEvent.getSource();
  }

  @FXML
  private void selDashboardFour(MouseEvent mouseEvent) {
    ImageView source = (ImageView) mouseEvent.getSource();
  }

  private void showDashboard(Dashboard dashboard) {
  }

  @FXML
  private void quitGameAction(ActionEvent actionEvent) {
    Controller.getController().disconnect();
    gui.setScene(SceneEnum.MENU);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
  }

  @Override
  public void updateAll() {
    super.updateAll();
    playerGridHandler.update();
    mainDashboardHandler.update();
    islandsGridHandler.update();
    cloudBoxHandler.update();
  }

  @Override
  public void finish() {
    super.finish();
    Controller.getController().removeListener(this, GAMEDATA_EVENT.tag);
  }
}
