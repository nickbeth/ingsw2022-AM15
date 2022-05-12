package it.polimi.ingsw.eriantys.network;

public record MessageQueueEntry(
    Client client,
    Message message) {
}
