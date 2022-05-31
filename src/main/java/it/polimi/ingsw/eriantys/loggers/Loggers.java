package it.polimi.ingsw.eriantys.loggers;

import org.tinylog.Logger;
import org.tinylog.TaggedLogger;

public class Loggers {
  public static TaggedLogger clientLogger = Logger.tag("clientWriter");
  public static TaggedLogger serverLogger = Logger.tag("serverWriter");
  public static TaggedLogger modelLogger = Logger.tag("serverWriter");
}
