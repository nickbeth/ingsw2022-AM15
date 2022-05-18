package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.cli.View;
import it.polimi.ingsw.eriantys.model.GameInfo;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Optional;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.*;
import static it.polimi.ingsw.eriantys.cli.utils.Util.PADDING;

public class GameLobbyView extends View {
  private GameInfo gameInfo;

  public GameLobbyView(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
  }

  /**
   * @param o The output stream which the view will write to.
   */
  @Override
  public void draw(PrintStream o) {
    int computedLength = horizontalLength();
    o.append(DOWN_AND_RIGHT.glyph)
            .append(HORIZONTAL.toString().repeat(computedLength))
            .append(DOWN_AND_LEFT.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL.glyph)
            .append(PADDING)
            .append("Game info")
            .append(PADDING.repeat(computedLength - 10))
            .append(VERTICAL.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL_AND_RIGHT.glyph)
            .append(HORIZONTAL_DASH.toString().repeat(computedLength))
            .append(VERTICAL_AND_LEFT.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL.glyph)
            .append(PADDING)
            .append("Mode: ")
            .append(gameInfo.getMode().toString())
            .append(PADDING.repeat(computedLength - 13))
            .append(VERTICAL.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL.glyph)
            .append(PADDING)
            .append("Players:")
            .append(PADDING)
            .append((char) gameInfo.getMaxPlayerCount())
            .append(PADDING.repeat(computedLength - 11))
            .append(VERTICAL.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL_SINGLE_AND_RIGHT_DOUBLE.glyph)
            .append(DOUBLE_HORIZONTAL.toString().repeat(computedLength))
            .append(VERTICAL_SINGLE_AND_LEFT_DOUBLE.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL.glyph)
            .append(PADDING)
            .append("Joined players:")
            .append(PADDING.repeat(computedLength - 16))
            .append(VERTICAL.glyph)
            .append(System.lineSeparator())
            .append(VERTICAL_AND_RIGHT.glyph)
            .append(HORIZONTAL_DASH.toString().repeat(computedLength))
            .append(VERTICAL_AND_LEFT.glyph)
            .append(System.lineSeparator());
    gameInfo.getJoinedPlayers().forEach((key, value) -> {
      o.append(VERTICAL.glyph)
              .append(PADDING)
              .append(key)
              .append(PADDING)
              .append("->")
              .append(PADDING);
      if (value != null)
        o.append(value.toString())
                .append(PADDING.repeat(computedLength - value.toString().length() - key.length() - 5));
      else o.append("empty")
              .append(PADDING.repeat(computedLength - key.length() - 10));
      o.append(VERTICAL.glyph).append(System.lineSeparator());
    });
    o.append(UP_AND_RIGHT.glyph)
            .append(HORIZONTAL.toString().repeat(computedLength))
            .append(UP_AND_LEFT.glyph)
            .append(System.lineSeparator());
  }

  private int horizontalLength() {
    Optional<String> name = gameInfo.getPlayers().stream().max(Comparator.comparingInt(String::length));
    int length;
    if (name.isPresent()) {
      length = name.get().length();
    } else return 17;
    if (length < 6) return 17;
    else return length + 11;
  }

}
