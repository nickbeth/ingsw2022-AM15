package it.polimi.ingsw.eriantys.network;

public record MessageQueueEntry(
    Client client,
    Message message) {

  @Override
  public String toString() {
    return "MessageQueueEntry{" +
        "client=" + client +
        ", message=" + message +
        '}';
  }
}
