package it.polimi.ingsw.eriantys.view.cli.utils;

public enum BoxSymbols {
  // Regular symbols
  DOWN_AND_RIGHT('┌'),
  DOWN_AND_LEFT('┐'),
  UP_AND_RIGHT('└'),
  UP_AND_LEFT('┘'),
  HORIZONTAL('─'),
  VERTICAL('│'),

  // Regular arcs
  ARC_DOWN_AND_RIGHT('╭'),
  ARC_DOWN_AND_LEFT('╮'),
  ARC_UP_AND_RIGHT('╰'),
  ARC_UP_AND_LEFT('╯'),

  // Pure double line symbols
  DOUBLE_DOWN_AND_RIGHT('╔'),
  DOUBLE_DOWN_AND_LEFT('╗'),
  DOUBLE_UP_AND_RIGHT('╚'),
  DOUBLE_UP_AND_LEFT('╝'),
  DOUBLE_HORIZONTAL('═'),
  DOUBLE_VERTICAL('║'),

  // Mixed double/single symbols
  VERTICAL_SINGLE_AND_RIGHT_DOUBLE('╞'),
  DOUBLE_SINGLE_AND_RIGHT_SINGLE('╟'),
  UP_SINGLE_AND_HORIZONTAL_DOUBLE('╧'),
  DOWN_SINGLE_AND_HORIZONTAL_DOUBLE('╤'),
  DOUBLE_UP_AND_HORIZONTAL('╩'),
  DOUBLE_DOWN_AND_HORIZONTAL('╦');

  public final char glyph;

  BoxSymbols(char glyph) {
    this.glyph = glyph;
  }

  @Override
  public String toString() {
    return Character.toString(glyph);
  }
}
