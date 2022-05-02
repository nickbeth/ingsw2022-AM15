package it.polimi.ingsw.eriantys.network;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
  private static final int ROOM_CODE_LENGTH = 4;
  private static final String ROOM_CODE_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

  public static String generateRoomCode() {
    StringBuilder code = new StringBuilder();
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    while (code.length() < ROOM_CODE_LENGTH) {
      int index = (int) (rnd.nextFloat() * ROOM_CODE_CHARS.length());
      code.append(ROOM_CODE_CHARS.charAt(index));
    }
    return code.toString();
  }
}
