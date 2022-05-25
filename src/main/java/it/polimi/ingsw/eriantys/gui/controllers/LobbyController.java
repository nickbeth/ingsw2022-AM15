package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.gui.SceneEnum;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.eriantys.controller.EventType.GAMEDATA_EVENT;
import static it.polimi.ingsw.eriantys.controller.EventType.GAMEINFO_EVENT;

public class LobbyController extends FXMLController {
  @FXML
  private Label errorMessage;
  @FXML
  private ChoiceBox<TowerColor> towerColorChoice;
  @FXML
  private ListView<String> playerList;
  @FXML
  private Label gameCode;
  @FXML
  private Label gameMode;
  @FXML
  private Label maxPlayerCount;

  @FXML
  private void startGameAction(ActionEvent actionEvent) {
    if(!gui.getController().sender().sendStartGame()) {
      errorMessage.setText("not enough players with a chosen color to start");
      errorMessage.setVisible(true);
    }
  }

  @FXML
  public void changeColorAction(ActionEvent actionEvent) {
    gui.getController().sender().sendSelectTower(towerColorChoice.getValue());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(Objects.equals(evt.getPropertyName(), GAMEDATA_EVENT.tag))
      gui.setScene(SceneEnum.MENU); //provvisorio
    else
      updateAll();
  }

  @Override
  public void updateAll() {
    playerList.getItems().clear();
    towerColorChoice.getItems().clear();
    GameInfo gameInfo = gui.getController().getGameInfo();
    Map<String, TowerColor> playerMap = gameInfo.getPlayersMap();
    gameCode.setText(gui.getController().getGameCode().toString());
    gameMode.setText(gameInfo.getMode().toString());
    maxPlayerCount.setText(String.valueOf(gameInfo.getMaxPlayerCount()));
    for (String player : playerMap.keySet()) {
      TowerColor color = playerMap.get(player);
      String colorStr = "";
      if (color != null)
        colorStr = color.toString();
      playerList.getItems().add(player + " " + colorStr);
    }
    for (TowerColor color : TowerColor.values()) {
      if (gameInfo.isTowerColorValid(gui.getController().getNickname(), color))
        towerColorChoice.getItems().add(color);
    }
    towerColorChoice.getItems().add(null);
  }

  @Override
  public void start() {
    super.start();
    gui.getController().addListener(this, GAMEINFO_EVENT.tag);
  }

  @Override
  public void finish() {
    super.finish();
    gui.getController().removeListener(this, GAMEINFO_EVENT.tag);
  }
}
