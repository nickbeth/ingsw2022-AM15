package it.polimi.ingsw.eriantys.server;

import it.polimi.ingsw.eriantys.network.Server;

public class ServerArgs {
  public static final int DEFAULT_DELETE_TIMEOUT = 30000; //!< 30 seconds
  public int port = Server.DEFAULT_PORT;
  public boolean heartbeat = true;
  public int deleteTimeout = DEFAULT_DELETE_TIMEOUT;
}
