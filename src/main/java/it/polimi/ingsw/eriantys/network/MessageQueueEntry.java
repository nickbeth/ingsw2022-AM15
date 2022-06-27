package it.polimi.ingsw.eriantys.network;

/**
 * An entry in the message queue, containing the message and the client that sent it.
 *
 * @param client  The client that sent this message
 * @param message The received message
 */
public record MessageQueueEntry(
    Client client,
    Message message
) {

  @Override
  public String toString() {
    return "MessageQueueEntry[client=" + client +
        ", message=" + message +
        "]";
  }
}
