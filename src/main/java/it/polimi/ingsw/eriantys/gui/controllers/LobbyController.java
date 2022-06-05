package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.controller.Controller;
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

    if(!Controller.get().sender().sendStartGame()) {
      errorMessage.setText("not enough players with a chosen color to start");
      errorMessage.setVisible(true);
    }
  }

  @FXML
  public void changeColorAction(ActionEvent actionEvent) {
    Controller.get().sender().sendSelectTower(towerColorChoice.getValue());
  }

  @FXML
  private void quitLobbyAction(ActionEvent actionEvent) {
    Controller.get().disconnect();
    gui.setScene(SceneEnum.MENU);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(GAMEDATA_EVENT.tag))
      gui.setScene(SceneEnum.PLANNING); //provvisorio
    else
      updateAll();
  }
  @Override
  public void updateAll() {
    playerList.getItems().clear();
    towerColorChoice.getItems().clear();
    GameInfo gameInfo = Controller.get().getGameInfo();
    Map<String, TowerColor> playerMap = gameInfo.getPlayersMap();
    gameCode.setText(Controller.get().getGameCode().toString());
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
      if (gameInfo.isTowerColorValid(Controller.get().getNickname(), color))
        towerColorChoice.getItems().add(color);
    }
    towerColorChoice.getItems().add(null);
  }

  @Override
  public void start() {
    super.start();
    Controller.get().addListener(this, GAMEDATA_EVENT.tag);
    Controller.get().addListener(this, GAMEINFO_EVENT.tag);
  }

  @Override
  public void finish() {
    super.finish();
    Controller.get().removeListener(this, GAMEINFO_EVENT.tag);
    Controller.get().removeListener(this, GAMEDATA_EVENT.tag);
  }
}
