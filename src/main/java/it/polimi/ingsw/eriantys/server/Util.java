package it.polimi.ingsw.eriantys.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
  private static final int GAME_CODE_LENGTH = 4;
  private static final String GAME_CODE_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

  /**
   * Stores all used room codes to avoid duplicates.
   */
  private static final Set<String> usedGameCodes = new HashSet<>();

  public static String generateGameCode() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    StringBuilder code = new StringBuilder();
    String finalCode;
    boolean unique;
    // Keep track of used room codes to avoid duplicates
    do {
      while (code.length() < GAME_CODE_LENGTH) {
        int index = rnd.nextInt(GAME_CODE_CHARS.length());
        code.append(GAME_CODE_CHARS.charAt(index));
      }
      finalCode = code.toString();

      unique = !usedGameCodes.contains(finalCode);
    } while (!unique);

    usedGameCodes.add(finalCode);
    return finalCode;
  }
}
