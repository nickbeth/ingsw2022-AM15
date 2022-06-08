package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.controller.Controller;

import java.beans.PropertyChangeSupport;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.INPUT_ENTERED;
import static it.polimi.ingsw.eriantys.loggers.Loggers.clientLogger;

public class InputHandler implements Runnable {
  private final PropertyChangeSupport support;
  private static InputHandler inputHandler;
  private final Scanner scanner = new Scanner(System.in);

  private String line = "";

  private InputHandler(PropertyChangeSupport support) {
    this.support = support;
  }

  public static InputHandler get() {
    if (inputHandler == null)
      inputHandler = new InputHandler(Controller.get().getListenerHolder());
    return inputHandler;
  }

  @Override
  public void run() {
    // Starting input handler
    while (!line.equals("quit")) {
      line = scanner.nextLine();
      support.firePropertyChange(INPUT_ENTERED.tag, null, line);
      clientLogger.debug("Thrown INPUT_ENTERED event line taken: " + line);
    }
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }
}
