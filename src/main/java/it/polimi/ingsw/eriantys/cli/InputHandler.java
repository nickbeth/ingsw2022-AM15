package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.controller.Controller;

import java.beans.PropertyChangeSupport;
import java.util.Scanner;

import static it.polimi.ingsw.eriantys.controller.EventType.INPUT_ENTERED;

public class InputHandler implements Runnable {
  private final PropertyChangeSupport support;
  private static InputHandler inputHandler;

  private String line = "";

  private InputHandler(PropertyChangeSupport support) {
    this.support = support;
  }

  public static InputHandler getInputHandler() {
    if (inputHandler == null)
      inputHandler = new InputHandler(Controller.get().getListenerHolder());
    return inputHandler;
  }

  @Override
  public void run() {
    // Starting input handler
    Scanner scanner = new Scanner(System.in);
    while (!line.equals("quit")) {
//      System.out.println("Input handler: ");
      line = scanner.nextLine();
      support.firePropertyChange(INPUT_ENTERED.tag, null, line);
    }
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }
}
