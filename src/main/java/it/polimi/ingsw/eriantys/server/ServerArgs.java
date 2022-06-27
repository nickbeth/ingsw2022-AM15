package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.Server;

public class ServerArgs {
  public static final int DEFAULT_DELETE_TIMEOUT = 30000; //!< 30 seconds

  /**
   * Port to listen on
   */
  public int port = Server.DEFAULT_PORT;

  /**
   * Whether heartbeat messages should be used to keep track of clients disconnections or not
   */
  public boolean heartbeat = true;

  /**
   * Time to wait before deleting an idle game entry (when only one player is left)
   */
  public int deleteTimeout = DEFAULT_DELETE_TIMEOUT;
}
