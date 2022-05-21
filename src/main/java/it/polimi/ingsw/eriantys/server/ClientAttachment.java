package it.polimi.ingsw.eriantys.server;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An object to be attached to a {@link it.polimi.ingsw.eriantys.network.Client} for holding
 * data about the game this client is connected to.
 */
public class ClientAttachment {
  private final String nickname;
  private String gameCode;
  private final AtomicInteger missedHeartbeatCount;

  public ClientAttachment(String nickname) {
    this.nickname = nickname;
    this.missedHeartbeatCount = new AtomicInteger(0);
  }

  public String nickname() {
    return nickname;
  }

  public String gameCode() {
    return gameCode;
  }

  public void setGameCode(String gameCode) {
    this.gameCode = gameCode;
  }

  /**
   * Atomically increments the missed heartbeat count value and returns it.
   *
   * @return The missed heartbeat count value after being incremented
   */
  public int increaseMissedHeartbeatCount() {
    return missedHeartbeatCount.incrementAndGet();
  }

  /**
   * Atomically resets the missed heartbeat count value to 0.
   */
  public void resetMissedHeartbeatCount() {
    missedHeartbeatCount.set(0);
  }
}
