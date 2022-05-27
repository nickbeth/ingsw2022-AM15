package it.polimi.ingsw.eriantys.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameCodeTest {
  @Test
  void equals() {
    GameCode code1 = new GameCode("AAAA");
    GameCode code2 = new GameCode("AAAA");
    String codeString = "AAAA";
    // Check for equality
    assertEquals(code1, code2);
    // Check for equality between different types
    //noinspection AssertBetweenInconvertibleTypes
    assertNotEquals(code1, codeString);
  }

  @Test
  void tostring() {
    GameCode code = new GameCode("AB12");
    // Check for correct string representation
    assertEquals("AB12", code.toString());
  }

  @Test
  void hashcode() {
    GameCode code1 = new GameCode("AAAA");
    GameCode code2 = new GameCode("AAAA");
    // Check that GameCode hashCode is the same as String hashCode
    assertEquals(code1.hashCode(), code1.toString().hashCode());
    // Check that two identical game codes produce the same hashCode
    assertEquals(code1.hashCode(), code2.hashCode());
  }

  @Test
  void parseCode() {
    GameCode parsedCode = GameCode.parseCode("UWHG");
    // Validate parseCode code correctness
    assertEquals(4, parsedCode.toString().length());
    for (int i = 0; i < parsedCode.toString().length(); i++) {
      assertNotEquals(-1, GameCode.GAME_CODE_CHARS.indexOf(parsedCode.toString().charAt(i)));
    }

    // Validate parseCode length exception
    try {
      GameCode.parseCode("UWHG1");
    } catch (GameCode.GameCodeException e) {
      assertEquals("Invalid code length: 5", e.getMessage());
    }

    // Validate parseCode character exception
    try {
      GameCode.parseCode("UWH0");
    } catch (GameCode.GameCodeException e) {
      assertEquals("Invalid character '0' in code 'UWH0'", e.getMessage());
    }
  }

  @Test
  void generate() {
    GameCode code = GameCode.generate();
    // Validate generated code correctness
    assertEquals(4, code.toString().length());
    for (int i = 0; i < code.toString().length(); i++) {
      assertNotEquals(-1, GameCode.GAME_CODE_CHARS.indexOf(code.toString().charAt(i)));
    }
  }
}
