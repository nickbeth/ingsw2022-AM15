package it.polimi.ingsw.eriantys.network;

import it.polimi.ingsw.eriantys.model.GameCode;
import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.actions.GameAction;

import java.io.Serializable;
import java.util.Objects;

/**
 * A data packet sent over the network for client-server communication.
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
    private String nickname;
    private GameCode gameCode;
    private GameInfo gameInfo;
    private String error;
    private GameAction gameAction;

    public Builder() {
    }

    public Builder(MessageType type) {
      this.type = type;
    }

    /**
     * Sets the type of this message.
     *
     * @return The builder instance
     */
    public Builder type(MessageType type) {
      this.type = type;
      return this;
    }

    /**
     * Sets the nickname of this message.
     *
     * @return The builder instance
     */
    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    /**
     * Sets the game code of this message.
     *
     * @return The builder instance
     */
    public Builder gameCode(GameCode gameCode) {
      this.gameCode = gameCode;
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
      this.nickname = null;
      this.gameCode = null;
      this.error = null;
      this.gameAction = null;
      this.gameInfo = null;
      return this;
    }

    /**
     * Builds a new message based on the given parameters.
     */
    public Message build() {
      return new Message(type, nickname, gameCode, error, gameAction, gameInfo);
    }
  }

  private final MessageType type;
  private final String nickname;
  private final GameCode gameCode;
  private final String error;
  private final GameAction gameAction;
  private final GameInfo gameInfo;

  protected Message(MessageType type, String nickname, GameCode gameCode, String error, GameAction gameAction, GameInfo gameInfo) {
    this.type = type;
    this.nickname = nickname;
    this.gameCode = gameCode;
    this.error = error;
    this.gameAction = gameAction;
    this.gameInfo = gameInfo;
  }

  /**
   * Initialises an empty message of the given type. Useful for subclasses that don't share any data with a base message.
   */
  protected Message(MessageType type) {
    this(type, null, null, null, null, null);
  }

  public MessageType type() {
    return type;
  }

  public String nickname() {
    return nickname;
  }

  public GameCode gameCode() {
    return gameCode;
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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null || getClass() != obj.getClass())
      return false;

    Message message = (Message) obj;
    return type == message.type &&
        Objects.equals(nickname, message.nickname) &&
        Objects.equals(gameCode, message.gameCode) &&
        Objects.equals(error, message.error) &&
        Objects.equals(gameAction, message.gameAction) &&
        Objects.equals(gameInfo, message.gameInfo);
  }

  @Override
  public String toString() {
    return "Message[type=" + type +
        ", nickname=" + nickname +
        ", gameCode=" + gameCode +
        ", error=" + error +
        ", action=" + (gameAction != null ? gameAction().getClass().getSimpleName() : null) +
        ", gameInfo=" + gameInfo +
        "]";
  }
}
