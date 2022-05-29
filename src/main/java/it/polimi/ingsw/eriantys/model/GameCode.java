package it.polimi.ingsw.eriantys.model;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a code that is used to join a game.
 */
public class GameCode implements Serializable {
  static final int GAME_CODE_LENGTH = 4;
  static final String GAME_CODE_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

  /**
   * Generates a new game code.
   *
   * @return The newly generated game code.
   */
  public static GameCode generate() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    StringBuilder code = new StringBuilder(GAME_CODE_LENGTH);

    for (int i = 0; i < GAME_CODE_LENGTH; i++) {
      code.append(GAME_CODE_CHARS.charAt(rnd.nextInt(GAME_CODE_CHARS.length())));
    }

    return new GameCode(code.toString());
  }

  /**
   * Returns a new, unique game code, compared against the given set of game codes.
   *
   * @param codeSet The set of game codes to perform comparisons with
   * @return The newly generated, unique game code
   */
  public static GameCode generateUnique(Set<GameCode> codeSet) {
    GameCode code = generate();
    while (codeSet.contains(code)) {
      code = generate();
    }
    return code;
  }

  /**
   * Parses the given string as a game code. The string must be {@link #GAME_CODE_LENGTH} characters long,
   * and must contain only characters from {@link #GAME_CODE_CHARS}.
   *
   * @param code The {@code String} containing the {@code GameCode} representation to be parsed
   * @return The game code represented by the given string
   * @throws GameCodeException If the string does not contain a parsable game code
   */
  public static GameCode parseCode(String code) throws GameCodeException {
    validateGameCode(code);
    return new GameCode(code);
  }

  public static void validateGameCode(String code) throws GameCodeException {
    if (code.length() != GAME_CODE_LENGTH) {
      throw new GameCodeException("Invalid code length: " + code.length());
    }

    for (int i = 0; i < code.length(); i++) {
      char c = code.charAt(i);
      if (GAME_CODE_CHARS.indexOf(c) == -1) {
        throw new GameCodeException("Invalid character '" + c + "' in code '" + code + "'");
      }
    }
  }

  public static class GameCodeException extends IllegalArgumentException {
    public GameCodeException() {
      super();
    }

    public GameCodeException(String message) {
      super(message);
    }
  }

  public final String code;

  public GameCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (obj == null || obj.getClass() != getClass())
      return false;

    GameCode c = (GameCode) obj;
    return code.equals(c.code);
  }

  @Override
  public int hashCode() {
    return code.hashCode();
  }
}
