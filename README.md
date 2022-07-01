# Software Engineering Project 2022 - AM15

Java implementation of the table
game [Eriantys: The Magical World of Floating Islands](https://craniointernational.com/products/eriantys/)

## Team members

- Niccolò Betto [lynxnb](https://github.com/lynxnb)
- Milo Brontesi [zibas-p](https://github.com/zibas-p)
- Gabriele Caliandro [gabriele-caliandro](https://github.com/gabriele-caliandro)

## Features state

| Feature              | State |
|----------------------|-------|
| 12 character cards   | 🔴    |
| 4 players            | 🟢    |
| Multiple games       | 🟢    |
| Game persistance     | 🔴    |
| Player reconnections | 🟢    |

### Legend:

```
🔴: Not planned
🟡: Work in progress
🟢: Ready
```
## Implementation description

Following is the rationale behind the central parts of our architecture. Only the most relevant implementation details
are reported here. We feel like this short description could be helpful for understanding the choices behind the code in
this repository.

### Model

#### GameState

`GameState` is the main model class. Conceptually, it's a `Serializable` data class holding data
about the current state of the game, and provides the necessary methods to easily update its state
according to the game specifications. E.g. the `advance` method takes care of advancing the game phase
(`ACTION -> PLANNING`, etc...) based on the current player, the current phase, and so on.

At any point a `GameState` object may be read by a controller and reconstruct the entire player view.
Every client stores its own copy of the model, as well as the server. The server's one is the source of authority.

#### GameAction

Any modification to the game state (apart from initialization) is applied via `GameAction` objects.
`GameAction` is an abstract class, and its most important methods are:

* `apply(GameState)`: applies changes to the `GameState` instance provided
* `isValid(GameState)`: checks an arbitrary boolean condition on `GameState` values

We then extend the `GameAction` class to create an action for every move a player can perform. E.g: `PickAssistantCard`
extends `GameAction` and provides an implementation of the `apply` method that sets the played assistant card of a
player, while also providing an implementation of the `isValid` method that checks if the player could play that card.

Using the approach described above guarantees that any changes to the game state are applied
consistently across all clients (and server). When the user performs a game action, the following
happens:

* A `GameAction` object corresponding to the action performed by the player is constructed with the necessary parameters
* The action is sent to the server for validation
* The server calls the `isValid` method to check its validity
    * On valid action: the action is applied to the server's game state, and then sent back to all clients to be applied
      to their game state
    * On invalid action: an error message is returned

### Controller

The `Controller` abstract class contains all common methods for interacting with the server, as well storing the
current game state. The controller also acts as an observable object, which UIs subscribe to for refreshing their
displayed content.  
`CliController` and `GuiController` subclass `Controller`, both of which provide implementations/overrides for
controller methods which need special handling in one or both cases (thanks, JavaFX 🙏).

### Network

Clients exchange `Message` objects with the server. A thread is created for every client after acceptation, which keeps
looping, listening for messages incoming from the socket.

The main architecture of the network subsystem consists of a multithreaded producer-consumer pattern: incoming messages
are added to a queue by the socket thread, alongside the client that sent that message. The message queue is shared with
a message handler, running on its own thread, which will then consume the messages, aware of the client that sent it.

Every client has a `ClientAttachment` object attached to it, containing data about the player connected through that
client object (e.g. nickname, game code, etc...), therefore avoiding the need of a bulky map of all connected players.

See:

* [Client](https://github.com/lynxnb/ingsw2022-AM15/blob/master/src/main/java/it/polimi/ingsw/eriantys/network/Client.java#L129)
* [MessageQueueEntry](https://github.com/lynxnb/ingsw2022-AM15/blob/master/src/main/java/it/polimi/ingsw/eriantys/network/MessageQueueEntry.java)
* [GameServer](https://github.com/lynxnb/ingsw2022-AM15/blob/master/src/main/java/it/polimi/ingsw/eriantys/server/GameServer.java#L85)
* [ClientAttachment](https://github.com/lynxnb/ingsw2022-AM15/blob/master/src/main/java/it/polimi/ingsw/eriantys/server/ClientAttachment.java)

#### Message

`Message` is a serializable class with a fixed structure, and it is immutable after initial creation.
It is sent over the network via `Client.send` and received with `Client.receive`.
Messages have a `MessageType` attribute which acts as a header. Message handlers use this type attribute to handle each
message correctly.

See [MessageType](https://github.com/lynxnb/ingsw2022-AM15/blob/master/src/main/java/it/polimi/ingsw/eriantys/network/MessageType.java)

#### Socket abstraction

`Client` and `Server` classes are essentially wrappers around `java.net.Socket`, with the baked-in ability to send and
receive `Message` objects via `Object[Input|Output]Stream`.

#### MessageHandler – GameServer

Message handlers take messages from the message queue and perform actions relative to the message type. They run on
a separate thread.

The `MessageHandler` class performs message dispatching on the client, by reading data inside messages and firing
appropriate events for the controller to update.  
The `GameServer` performs the same task on the server, but with the added complexity of having to manage multiple
connected clients and multiple games.

### Advanced features

* `4 players`: a 4 players' game can be played
* `Multiple games`: every game has an associated game code (e.g. `ABCD`). The player must supply one when trying to join
  a game, and it is returned one after the creation of a new game. A list of available games to join is provided in UIs.
* `Player reconnections`: the server keep a heartbeat running with all clients (`PING` and `PONG` messages are used).
  Once the heartbeat fails, the client is marked as disconnected in the game lobby, and the game continues skipping
  disconnected players. A player may also disconnect voluntarily from a game. Once it is reconnected, a copy of the game
  state gets sent to that player.

### Possible improvements

All the features described below were discarded for time constraints reasons.

* One idea we had was to periodically hash the game state of clients and check that against the server's one. It would
  provide a strong guarantee that all game states are synchronized. In the case of hash mismatch, the server would
  simply send its own game state to the affected clients, in a completely transparent way to the player.
* We wanted to use classes from the `java.nio` package for more efficient network operations. That would imply using a
  `Selector` to select sockets available for read/write operations, instead of having one thread per socket always
  listening for incoming messages. The added complexity of having to deal with `ByteBuffer`s, the need to implement a
  low-level system to reconstruct `Message`s from an unknown amount of `ByteBuffer`s (which could also arrive in
  different time windows), made us reconsider this idea, and instead direct our efforts to other, more relevant aspects
  of the project.  
  Nonetheless, the concept of a `Client` having an `ClientAttachment` attached to it was borrowed from
  the `java.nio.SocketChannel` class. Therefore, even if the original idea was discarded, not all time spent on that
  went to waste, as we ended up reusing concepts and apply them to our specific case.
  A branch with a WIP version of `java.nio` networking is still available in our
  repo: [java-nio](https://github.com/lynxnb/ingsw2022-AM15/tree/java-nio), although it is abandoned at this point and
  can only send example messages.

## Running the server
The command to run the server is : ```java -jar server-1.0.jar```. The server default port is 1234.

Additional command line arguments:
| description          |Argument |
|----------------------|-------|
| To disable server heartbeat verification  | ```--no-heartbeat ```   |
| To run the server on a specific port    | ```-p``` or ```--port``` followed by the port number|
| To change the time to wait before deleting an idle game entry | ```-d``` or ``` --delete-timeout``` followed by the amount in seconds  |
## Running the client
The client can be run in GUI by opening the ```client-1.0.jar``` or by using the ```java -jar client-1.0.jar``` command.

To run the client in CLI the ```-c``` ```--cli``` argument is needed:  ```java -jar client-1.0.jar -c```.


