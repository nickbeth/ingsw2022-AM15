package it.polimi.ingsw.eriantys.gui.controllers;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.TowerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.beans.PropertyChangeEvent;
import java.util.Map;

import static it.polimi.ingsw.eriantys.controller.Controller.EventEnum.GAMEINFO_EVENT;

public class LobbyController extends FXMLController {

  @FXML
  private ListView playerList;
  @FXML
  private Label gameCode;
  @FXML
  private Label gameMode;
  @FXML
  private Label maxPlayerCount;

  @FXML
  private void startGameAction(ActionEvent actionEvent) {
  }

  @FXML
  public void changeColorAction(ActionEvent actionEvent) {
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    updateAll();
  }

  @Override
  public void updateAll() {
    playerList.getItems().clear();
    GameInfo gameInfo = gui.getController().getGameInfo();
    Map<String, TowerColor> playerMap = gameInfo.getPlayersMap();
    gameCode.setText(gui.getController().getGameCode());
    gameMode.setText(gameInfo.getMode().toString());
    maxPlayerCount.setText(String.valueOf(gameInfo.getMaxPlayerCount()));
    for (String player : playerMap.keySet()) {
      TowerColor color = playerMap.get(player);
      String colorStr = "";
      if (color != null)
        colorStr = color.toString();
      playerList.getItems().add(player + " " + colorStr);
    }
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
