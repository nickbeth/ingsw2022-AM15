package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.model.GameInfo;
import it.polimi.ingsw.eriantys.model.enums.GameMode;
import it.polimi.ingsw.eriantys.network.Client;
import it.polimi.ingsw.eriantys.network.Message;
import it.polimi.ingsw.eriantys.network.MessageQueueEntry;
import it.polimi.ingsw.eriantys.network.MessageType;
import org.javatuples.Pair;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.eriantys.model.GameCode.validateGameCode;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The tests in this class are meant to be executed in the order specified by the annotations.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {
  // Configuration
  private static final int TEST_PORT = 1235;
  private static final int CLIENT_COUNT = 2;

  private static ServerApp serverApp;
  private static Thread serverThread;
  private static ArrayList<Pair<Client, BlockingQueue<MessageQueueEntry>>> clients;

  @BeforeAll
  static void start() throws IOException {
    serverApp = new ServerApp(new ServerArgs());
    serverThread = new Thread(serverApp::run, "server");
    serverThread.start();
    // Let the server initialize
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {
    }

    // Populate the list of clients
    clients = new ArrayList<>();
    for (int i = 1; i <= CLIENT_COUNT; i++) {
      // Create a message queue for the client
      BlockingQueue<MessageQueueEntry> messageQueue = new LinkedBlockingQueue<>();
      // Create the client launch the listener thread
      var client = new Client(messageQueue);
      client.connect(null, TEST_PORT);
      new Thread(client, "client-" + i).start();
      // Add the client to the list
      clients.add(new Pair<>(client, messageQueue));
    }
  }

  @AfterAll
  static void stop() {
    serverApp.exitAndJoin();
    assertFalse(serverThread.isAlive());
  }

  @Test()
  @Order(1)
  public void nicknameCollision() throws InterruptedException {
    var clientPair = clients.get(0);
    var client1 = clientPair.getValue0();
    var messageQueue1 = clientPair.getValue1();

    clientPair = clients.get(1);
    var client2 = clientPair.getValue0();
    var messageQueue2 = clientPair.getValue1();

    Message nicknameReq = new Message.Builder().type(MessageType.NICKNAME_REQUEST).nickname("test1").build();
    client1.send(nicknameReq);
    assertEquals(messageQueue1.take().message(), new Message.Builder().type(MessageType.NICKNAME_OK).nickname("test1").build());
    client2.send(nicknameReq);
    assertEquals(messageQueue2.take().message(), new Message.Builder().type(MessageType.ERROR).error("Nickname 'test1' is already in use").build());
  }

  @Test()
  @Order(2)
  public void createGame() throws InterruptedException {
    var clientPair = clients.get(0);
    var client1 = clientPair.getValue0();
    var messageQueue1 = clientPair.getValue1();

    GameInfo gameInfo = new GameInfo(2, GameMode.NORMAL);
    Message createGameReq = new Message.Builder().type(MessageType.CREATE_GAME).nickname("test1").gameInfo(gameInfo).build();
    gameInfo.addPlayer("test1");

    client1.send(createGameReq);
    Message reply = messageQueue1.take().message();
    assertEquals(reply.gameInfo(), gameInfo);
    assertDoesNotThrow(() -> validateGameCode(reply.gameCode().toString()));
  }

  @Test
  @Order(3)
  public void createAnotherGame() throws InterruptedException {
    if (CLIENT_COUNT < 2)
      return;

    var clientPair = clients.get(1);
    var client2 = clientPair.getValue0();
    var messageQueue2 = clientPair.getValue1();

    Message nicknameReq = new Message.Builder().type(MessageType.NICKNAME_REQUEST).nickname("test2").build();
    client2.send(nicknameReq);
    assertEquals(messageQueue2.take().message(), new Message.Builder().type(MessageType.NICKNAME_OK).nickname("test2").build());

    GameInfo gameInfo = new GameInfo(3, GameMode.EXPERT);
    Message createGameReq = new Message.Builder().type(MessageType.CREATE_GAME).nickname("test2").gameInfo(gameInfo).build();
    gameInfo.addPlayer("test2");

    client2.send(createGameReq);
    Message reply = messageQueue2.take().message();
    assertEquals(reply.type(), MessageType.GAMEINFO);
    assertEquals(reply.gameInfo(), gameInfo);
    assertDoesNotThrow(() -> validateGameCode(reply.gameCode().toString()));
  }
}
