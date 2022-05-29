package it.polimi.ingsw.eriantys.gui.controllers.utils;

public enum IslandPattern {
  TWELVE(new int[][]{
          {0, 1, 2, 3, -1},
          {4, -1, -1, 5, -1},
          {6, -1, -1, 7, -1},
          {8, 9, 10, 11, -1}}, 12),
  ELEVEN(new int[][]{
          {0, 1, 2, 3, -1},
          {4, -1, -1, 5, -1},
          {6, -1, -1, 7, -1},
          {-1, 9, 10, 11, -1}
  }, 11),
  TEN(new int[][]{
          {-1, 0, 1, 2, -1},
          {3, -1, -1, -1, 4},
          {5, -1, -1, -1, 6},
          {-1, 7, 8, 9, -1}
  }, 10),
  NINE(new int[][]{
          {-1, 0, 1, 2, -1},
          {3, -1, -1, -1, 4},
          {5, -1, -1, -1, 6},
          {-1, -1, 8, 9, -1}}, 9),
  EIGHT(new int[][]{
          {-1, 0, 1, -1, -1},
          {2, -1, -1, 3, -1},
          {4, -1, -1, 5, -1},
          {-1, 6, 7, -1, -1}
  }, 8);
  //TODO:other patterns for other amounts
  public final int[][] matrix;
  public final int amount;

  IslandPattern(int[][] matrix, int amount) {
    this.matrix = matrix;
    this.amount = amount;
  }

  public static IslandPattern getPattern(int amount){
    for (IslandPattern pattern : IslandPattern.values()) {
      if(pattern.amount == amount) return pattern;
    }
    return null;
  }
}
