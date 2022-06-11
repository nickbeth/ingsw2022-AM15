package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.GameState;
import it.polimi.ingsw.eriantys.model.entities.Player;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

public class PlayerGridHandler extends SectionHandler {
  private final GridPane playerGrid;
  private final GameState gameState = Controller.get().getGameState();

  private final HashMap<ImageView, Player> discIcons = new HashMap<>();
  private final HashMap<Label, Player> cardAmounts = new HashMap<>();
  private final HashMap<Label, Player> towerAmounts = new HashMap<>();
  private final HashMap<Label, Player> coinAmounts = new HashMap<>();

  public PlayerGridHandler(GridPane playerGrid, DebugScreenHandler debugScreenHandler) {
    this.playerGrid = playerGrid;
  }

  /**
   * Refreshes disconnect icon visibility, and numbers on card and tower counters. <br>
   * If the game is in EXPERT mode it refreshes coin counter
   */
  protected void refresh() {
    discIcons.forEach((img, player) -> {
      img.setVisible(!player.isConnected());
    });

    cardAmounts.forEach((label, player) -> {
      label.setText("×" + player.getCards().size());
    });

    towerAmounts.forEach((label, player) -> {
      label.setText("×" + player.getDashboard().getTowers().count);
    });

    if (gameState.getRuleBook().gameMode == GameMode.EXPERT)
      coinAmounts.forEach((label, player) -> {
        label.setText("×" + player.getCoins());
      });
  }

  @Override
  protected void create() {
    playerGrid.getStyleClass().add("grid-players");
    List<Player> players = gameState.getPlayers();
    int i = 0;
    int pIndex = 0;
    for (Player player : players) {
      if (!player.getNickname().equals(Controller.get().getNickname())) {
        StackPane icon = new StackPane();
        ImageView wizardIcon = new ImageView(new Image("/assets/wizards/wizard-" + pIndex + ".jpg"));
        wizardIcon.setFitWidth(40);
        wizardIcon.setPreserveRatio(true);
        wizardIcon.getStyleClass().add("image-wizard");
        icon.getChildren().add(wizardIcon);
        ImageView discIcon = new ImageView(new Image("/assets/misc/disconnected-icon.png"));
        discIcon.setFitWidth(10);
        discIcon.setPreserveRatio(true);
        discIcon.setVisible(false);
        icon.getChildren().add(discIcon);
        discIcons.put(discIcon, player);
        Label nickname = new Label(player.getNickname(), icon);
        nickname.getStyleClass().add("label-nicknames");
        nickname.setCursor(Cursor.HAND);
        nickname.setContentDisplay(ContentDisplay.RIGHT);
        //makes trasparent parts not clickable
        nickname.setPickOnBounds(false);
        //TODO: add show showDashboard action on click image, maybe it depends on the grid?
        playerGrid.add(nickname, 0, i);
        GridPane.setHalignment(nickname, HPos.RIGHT);

        VBox counters = new VBox();
        counters.setPadding(new Insets(3, 0, 0, 0));
        ImageView cardIcon = new ImageView(new Image("/assets/misc/card-icon.png"));
        cardIcon.setFitWidth(20);
        cardIcon.setPreserveRatio(true);
        Label cardAmount = new Label("×" + player.getCards().size(), cardIcon);
        cardAmount.getStyleClass().add("label-cardamount");
        cardAmounts.put(cardAmount, player);
        counters.getChildren().add(cardAmount);

        ImageView towerIcon = new ImageView(new Image("/assets/realm/tower-" + player.getColorTeam() + ".png"));
        towerIcon.setFitWidth(8);
        towerIcon.setPreserveRatio(true);
        Label towerAmount = new Label("×" + player.getDashboard().getTowers().count, towerIcon);
        VBox.setMargin(towerAmount, new Insets(0, 0, 0, 3));
        towerAmounts.put(towerAmount, player);
        counters.getChildren().add(towerAmount);

        if (gameState.getRuleBook().gameMode == GameMode.EXPERT) {
          ImageView coinIcon = new ImageView(new Image("/assets/realm/coin-icon.png"));
          coinIcon.setFitWidth(20);
          coinIcon.setPreserveRatio(true);
          Label coinAmount = new Label("×" + player.getCoins(), coinIcon);
//          VBox.setMargin(coinAmount, new Insets(0, 0, 0, 0));
          coinAmounts.put(coinAmount, player);
          counters.getChildren().add(coinAmount);
        }

        playerGrid.add(counters, 1, i);
        i++;
      }
      pIndex++;
    }
    super.create();
  }

}
