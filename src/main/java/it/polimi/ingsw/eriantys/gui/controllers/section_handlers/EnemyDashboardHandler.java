package it.polimi.ingsw.eriantys.gui.controllers.section_handlers;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

public class EnemyDashboardHandler extends DashboardHandler {
  private final AnchorPane dashboardPane;

  public EnemyDashboardHandler(AnchorPane dashboardPane, String nickname, GridPane studentHallGrid, GridPane entranceGrid, GridPane professorGrid, TilePane towerTiles, DebugScreenHandler debugScreenHandler) {
    super(nickname, studentHallGrid, entranceGrid, professorGrid, towerTiles, debugScreenHandler);
    this.dashboardPane = dashboardPane;
  }

  @Override
  protected void refresh() {
    if (dashboardPane.isVisible()) {
      debugScreenHandler.showMessage("refreshing " + getNickname() + "'s dashboard");
      super.refresh();
    }
  }

  @Override
  protected void create() {
    super.create();
    createNicknameLable();
    createCloseButton();
  }

  private void createCloseButton() {
    ImageView closeIcon = new ImageView(new Image("/assets/misc/cross-icon.png"));
    closeIcon.setPreserveRatio(true);
    closeIcon.setFitWidth(27.0);
    closeIcon.setPickOnBounds(false);
    closeIcon.setOnMouseClicked(e -> dashboardPane.setVisible(false));
    dashboardPane.getChildren().add(closeIcon);
    AnchorPane.setTopAnchor(closeIcon, 0.0);
    AnchorPane.setRightAnchor(closeIcon, 0.0);
  }

  private void createNicknameLable() {
    Label dashboardName = new Label(getNickname() + "'s dashboard");
    dashboardName.getStyleClass().add("label-dashname");
    dashboardPane.getChildren().add(dashboardName);
    dashboardName.setPrefWidth(313);
    dashboardName.setMinWidth(313);
    dashboardName.setAlignment(Pos.CENTER);
    dashboardName.setTextAlignment(TextAlignment.CENTER);
    AnchorPane.setTopAnchor(dashboardName, 0.0);
    AnchorPane.setLeftAnchor(dashboardName, 0.0);
  }

  public void show() {
    dashboardPane.setVisible(true);
    update();
  }

}
