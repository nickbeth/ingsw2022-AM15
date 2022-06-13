package it.polimi.ingsw.eriantys.loggers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Loggers {
  public static final Logger clientLogger
      = (Logger) LoggerFactory.getLogger("client");
  public static final Logger serverLogger
      = (Logger) LoggerFactory.getLogger("server");
  public static final Logger modelLogger
      = (Logger) LoggerFactory.getLogger("model");
  public static final Logger testLogger
      = (Logger) LoggerFactory.getLogger("test");
}
