package it.polimi.ingsw.eriantys.cli;

import it.polimi.ingsw.eriantys.model.enums.HouseColor;
import jfxtras.scene.layout.HBox;

import java.io.OutputStream;
import java.io.PrintStream;

import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.colored;

public class CustomPrintStream extends PrintStream {
  public static CustomPrintStream out = new CustomPrintStream(System.out);

  public CustomPrintStream(OutputStream out) {
    super(out);
  }

  public void print(String s, HouseColor color) {
    super.print(colored(s, color));
  }

  public void println(String s, HouseColor color) {
    super.println(colored(s, color));
  }
}
