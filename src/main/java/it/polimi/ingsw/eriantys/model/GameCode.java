package it.polimi.ingsw.eriantys.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GameCode implements Serializable {
  private static final int GAME_CODE_LENGTH = 4;
  private static final String GAME_CODE_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

  /**
   * Stores all used room codes to avoid duplicates.
   */
  private static final Set<char[]> usedGameCodes = new HashSet<>();

  private static char[] generate() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    char[] code = new char[GAME_CODE_LENGTH];
    String finalCode;
    boolean unique;
    // Keep track of used room codes to avoid duplicates
    do {
      for (int i = 0; i < code.length; i++) {
        code[i] = GAME_CODE_CHARS.charAt(rnd.nextInt(GAME_CODE_CHARS.length()));
      }

      unique = !usedGameCodes.contains(code);
    } while (!unique);

    usedGameCodes.add(code);
    return code;
  }

  public static GameCode parseCode(String code) throws GameCodeException {
    char[] codeChars = code.toCharArray();
    if (codeChars.length != GAME_CODE_LENGTH) {
      throw new GameCodeException("Invalid code length: " + codeChars.length);
    }
    StringBuilder s = new StringBuilder();
    boolean invalid = false;
    for (char c : codeChars) {
      if (GAME_CODE_CHARS.indexOf(c) == -1) {
        invalid = true;
        s.append(c);
      }
    }
    if (invalid) throw new GameCodeException("Invalid character: " + s);

    return new GameCode(codeChars);
  }

  public static class GameCodeException extends IllegalArgumentException {
    public GameCodeException() {
      super();
    }

    public GameCodeException(String message) {
      super(message);
    }
  }

  public final char[] code;

  /**
   * Creates a newly generated game code.
   */
  public GameCode() {
    this.code = generate();
  }

  /**
   * Creates a game code backed by the given char array.
   */
  private GameCode(char[] code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return Arrays.toString(code);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (obj == null || obj.getClass() != getClass())
      return false;

    GameCode c = (GameCode) obj;
    return Arrays.equals(code, c.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(Arrays.toString(code));
  }
}
