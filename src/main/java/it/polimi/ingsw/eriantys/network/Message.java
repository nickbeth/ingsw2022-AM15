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
    private String gameCode;
    private String nickname;
    private String error;
    private GameAction gameAction;
    private GameInfo gameInfo;

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
     * Sets the game code of this message.
     *
     * @return The builder instance
     */
    public Builder gameCode(String gameCode) {
      this.gameCode = gameCode;
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
      this.gameCode = null;
      this.nickname = null;
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
      return new Message(type, gameCode, nickname, error, gameAction, gameInfo);
    }
  }

  private final MessageType type;
  private final String gameCode;
  private final String nickname;
  private final String error;
  private final GameAction gameAction;
  private final GameInfo gameInfo;

  private Message(MessageType type, String gameCode, String nickname, String error, GameAction gameAction, GameInfo gameInfo) {
    this.type = type;
    this.gameCode = gameCode;
    this.nickname = nickname;
    this.error = error;
    this.gameAction = gameAction;
    this.gameInfo = gameInfo;
  }

  public MessageType type() {
    return type;
  }

  public String nickname() {
    return nickname;
  }

  public String gameCode() {
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
  public String toString() {
    return "Message[type=" + type +
        ", gameCode=" + gameCode +
        ", nickname=" + nickname +
        ", action=" + gameAction().getClass().getSimpleName() +
        ", error=" + error +
        ", gameInfo=" + gameInfo +
        "]";
  }
}
