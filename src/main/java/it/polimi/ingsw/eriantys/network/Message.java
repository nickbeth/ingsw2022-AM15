package it.polimi.ingsw.eriantys.network;

import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.io.Serializable;

/**
 * This class contains all data that will be sent over the network for communication.
 *
 * @implNote The constructor of this class has been made private to avoid further alterations
 * of the message contents after its creation. The associated Builder class must be used to create new messages.
 */
public class Message implements Serializable {
  /**
   * Builder class for Message objects.
   */
  public static class Builder {
    private MessageType type;
    private String error;
    private GameAction gameAction;
    private GameInfo gameInfo;

    /**
     * Sets the type of this message. If the type is different from the previous one,
     * the underlying parameters will be reset.
     *
     * @param type The type of message
     * @return The builder instance
     * @see #reset()
     */
    public Builder type(MessageType type) {
      if (this.type != type) {
        reset();
      }
      this.type = type;
      return this;
    }

    /**
     * Sets the error string of this message.
     *
     * @return The builder instance
     */
    public Builder error(String error) {
      this.error = error;
      return this;
    }

    /**
     * Sets the game action of this message.
     *
     * @return The builder instance
     */
    public Builder action(GameAction action) {
      this.gameAction = action;
      return this;
    }

    /**
     * Sets the game info of this message.
     *
     * @return The builder instance
     */
    public Builder gameInfo(GameInfo info) {
      this.gameInfo = info;
      return this;
    }

    /**
     * Resets all underlying parameters.
     */
    public Builder reset() {
      this.type = null;
      this.error = null;
      this.gameAction = null;
      this.gameInfo = null;
      return this;
    }

    /**
     * Builds a new message based on the given parameters.
     */
    public Message build() {
      return new Message(type, error, gameAction, gameInfo);
    }
  }

  private final MessageType type;
  private final String error;
  private final GameAction gameAction;
  private final GameInfo gameInfo;

  private Message(MessageType type, String error, GameAction gameAction, GameInfo gameInfo) {
    this.type = type;
    this.error = error;
    this.gameAction = gameAction;
    this.gameInfo = gameInfo;
  }

  public MessageType type() {
    return type;
  }

  public String error() {
    return error;
  }

  public GameAction gameAction() {
    return gameAction;
  }

  public GameInfo gameInfo() {
    return gameInfo;
  }

  @Override
  public String toString() {
    return "Message[type=" + type + ", action=" + gameAction + ", error=" + error + ", gameInfo=" + gameInfo + "]";
  }
}
