package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameCode;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An object to be attached to a {@link it.polimi.ingsw.eriantys.network.Client} for holding
 * data about the player connected through this socket.
 */
public class ClientAttachment {
  private final String nickname;
  private GameCode gameCode;
  private volatile ScheduledFuture<?> heartbeatSchedule;
  /**
   * We can't use synchronized blocks because thread-unsafe operations might be happening outside this class.
   * We hold a lock to be acquired and released by external users instead.
   */
  private final ReentrantLock heartbeatLock;
  private final AtomicInteger missedHeartbeatCount;

  public ClientAttachment(String nickname) {
    this.nickname = nickname;
    this.gameCode = null;
    this.heartbeatSchedule = null;
    this.heartbeatLock = new ReentrantLock(true);
    this.missedHeartbeatCount = new AtomicInteger(0);
  }

  public String nickname() {
    return nickname;
  }

  public GameCode gameCode() {
    return gameCode;
  }

  public void setGameCode(GameCode gameCode) {
    this.gameCode = gameCode;
  }

  /**
   * Acquires the heartbeat lock.
   */
  public void acquireHeartbeatLock() {
    heartbeatLock.lock();
  }

  /**
   * Releases the heartbeat lock.
   */
  public void releaseHeartbeatLock() {
    heartbeatLock.unlock();
  }

  /**
   * Cancel the current heartbeat schedule.
   *
   * @apiNote In a multithreaded environment, this method should only be called
   * after acquiring the heartbeat lock via {@link #acquireHeartbeatLock}
   * and releasing it via {@link #releaseHeartbeatLock}
   */
  public void cancelHeartbeatSchedule() {
    heartbeatSchedule.cancel(true);
  }

  /**
   * @return The current heartbeat cancelled state
   * @apiNote In a multithreaded environment, this method should only be called
   * after acquiring the heartbeat lock via {@link #acquireHeartbeatLock}
   * and releasing it via {@link #releaseHeartbeatLock}
   */
  public boolean isHeartbeatCancelled() {
    return heartbeatSchedule.isCancelled();
  }

  /**
   * Sets the current heartbeat schedule to the given {@link ScheduledFuture}.
   *
   * @apiNote In a multithreaded environment, this method should only be called
   * after acquiring the heartbeat lock via {@link #acquireHeartbeatLock}
   * and releasing it via {@link #releaseHeartbeatLock}
   */
  public void setHeartbeatSchedule(ScheduledFuture<?> newSchedule) {
    heartbeatSchedule = newSchedule;
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
