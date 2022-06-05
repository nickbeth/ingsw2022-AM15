package it.polimi.ingsw.eriantys.gui.controllers.SectionHandlers;

import it.polimi.ingsw.eriantys.controller.Controller;
import it.polimi.ingsw.eriantys.model.entities.Player;
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
  private GridPane playerGrid;

  private HashMap<ImageView, Player> discIcons = new HashMap<>();
  private HashMap<Label, Player> cardAmounts = new HashMap<>();
  private HashMap<Label, Player> towerAmounts = new HashMap<>();

  public PlayerGridHandler(GridPane playerGrid) {
    this.playerGrid = playerGrid;
  }

  /**
   * Refreshes disconnect icon visibility, and numbers on card and tower counters
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
  }

  @Override
  protected void create() {
    playerGrid.getStyleClass().add("grid-players");
    List<Player> players = Controller.get().getGameState().getPlayers();
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
        //TODO: add show showDashboard action on click to image, maybe it depends on the grid?
        playerGrid.add(nickname, 0, i);
        GridPane.setHalignment(nickname, HPos.RIGHT);

        VBox counters = new VBox();
        counters.setPadding(new Insets(5, 0, 0, 0));
        ImageView cardIcon = new ImageView(new Image("/assets/misc/card-icon.png"));
        cardIcon.setFitWidth(20);
        cardIcon.setPreserveRatio(true);
        Label cardAmount = new Label("×" + player.getCards().size(), cardIcon);
        cardAmount.getStyleClass().add("label-cardamount");
        cardAmounts.put(cardAmount, player);
        counters.getChildren().add(cardAmount);

        ImageView towerIcon = new ImageView(new Image("/assets/realm/tower-" + player.getColorTeam() + ".png"));
        towerIcon.setFitWidth(10);
        towerIcon.setPreserveRatio(true);
        Label towerAmount = new Label("×" + player.getDashboard().getTowers().count, towerIcon);
        VBox.setMargin(towerAmount, new Insets(3, 0, 0, 0));
        counters.getChildren().add(towerAmount);
        playerGrid.add(counters, 1, i);
        i++;
      }
      pIndex++;
    }
    super.create();
  }

}
