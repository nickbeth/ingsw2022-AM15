package it.polimi.ingsw.eriantys.gui.controllers.utils;

public enum IslandPattern {
  TWELVE(new int[][]{
          {0, 1, 2, 3, -1},
          {11, -1, -1, 4, -1},
          {10, -1, -1, 5, -1},
          {9, 8, 7, 6, -1}}, 12),
  ELEVEN(new int[][]{
          {0, 1, 2, 3, -1},
          {10, -1, -1, 4, -1},
          {9, -1, -1, 5, -1},
          {-1, 8, 7, 6, -1}
  }, 11),
  TEN(new int[][]{
          {-1, 0, 1, 2, -1},
          {-1, 9, -1, 3, -1},
          {-1, 8, -1, 4, -1},
          {-1, 7, 6, 5, -1}}, 10),
  NINE(new int[][]{
          {-1, 0, 1, 2, -1},
          {-1, 8, -1, 3, -1},
          {-1, 7, -1, 4, -1},
          {-1, -1, 6, 5, -1}}, 9),
  EIGHT(new int[][]{
          {-1, 0, 1, -1, -1},
          {7, -1, -1, 2, -1},
          {6, -1, -1, 3, -1},
          {-1, 5, 4, -1, -1}}, 8),
  SEVEN(new int[][]{
          {-1, 0, 1, -1, -1},
          {-1, -1, -1, 2, -1},
          {6, -1, -1, 3, -1},
          {-1, 5, 4, -1, -1}}, 7),
  SIX(new int[][]{
          {-1, -1, -1, -1, -1},
          {-1, 0, 1, 2, -1},
          {-1, 5, 4, 3, -1},
          {-1, -1, 7, -1, -1}}, 6),
  FIVE(new int[][]{
          {-1, -1, -1, -1, -1},
          {-1, 0, 1, 2, -1},
          {-1, -1, 4, 3, -1},
          {-1, -1, -1, -1, -1}}, 5),
  FOUR(new int[][]{
          {-1, -1, -1, -1, -1},
          {-1, -1, 0, -1, -1},
          {-1, 3, -1, 1, -1},
          {-1, -1, 2, -1, -1}}, 4),
  THREE(new int[][]{
          {-1, -1, -1, -1, -1},
          {-1, 0, 1, 2, -1},
          {-1, -1, -1, -1, -1},
          {-1, -1, -1, -1, -1}}, 3);
  //TODO:other patterns for other amounts
  public final int[][] matrix;
  public final int amount;

  IslandPattern(int[][] matrix, int amount) {
    this.matrix = matrix;
    this.amount = amount;
  }

  public static IslandPattern getPattern(int amount) {
    for (IslandPattern pattern : IslandPattern.values()) {
      if (pattern.amount == amount) return pattern;
    }
    return null;
  }
}
