package it.polimi.ingsw.eriantys.cli.views;

import it.polimi.ingsw.eriantys.model.entities.Cloud;
import it.polimi.ingsw.eriantys.model.enums.HouseColor;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.eriantys.cli.utils.BoxSymbols.VERTICAL;
import static it.polimi.ingsw.eriantys.cli.utils.PrintUtils.*;

public class CloudsView extends View {
  private final List<Cloud> clouds;
  private final String PADDING_FROM_EACH_CLOUD = PADDING_TRIPLE;

  public CloudsView(List<Cloud> clouds) {
    this.clouds = clouds;
  }

  @Override
  public void draw(PrintStream o) {
    int rows = drawCloud(clouds.get(0), 0).split(System.lineSeparator()).length;
    String[][] matrix = new String[clouds.size()][rows];

    for (int i = 0; i < clouds.size(); i++) {
      matrix[i] = drawCloud(clouds.get(i), i).split(System.lineSeparator());
    }

    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      for (String[] cloud : matrix) {
        stringBuilder
            .append(cloud[row])
            .append(PADDING_FROM_EACH_CLOUD);
      }
      stringBuilder
          .append(System.lineSeparator());
    }

    o.append(System.lineSeparator());

    // Title of the section
    o.append(centredTitle("CLOUDS")).append(System.lineSeparator());

    // Write the content
    o.append(stringBuilder);

    // Writes a "-" separator
    o.append(centredTitle("")).append(System.lineSeparator());
  }

  private String drawCloud(Cloud cloud, int index) {
    LinkedList<HouseColor> studentsList = cloud.getStudents().toLinkedList();
    StringBuilder stringBuilder = new StringBuilder();

    // First row
    if (index >= 0) {
      stringBuilder.append(MessageFormat.format("╭──{0}──╮", index));
    }
    stringBuilder
        .append(System.lineSeparator())
        .append(VERTICAL.glyph)
        .append(PADDING)
        .append(printColored(STUDENT_CHAR, studentsList.poll()))
        .append(PADDING)
        .append(printColored(STUDENT_CHAR, studentsList.poll()))
        .append(PADDING)
        .append(VERTICAL.glyph)
        .append(System.lineSeparator());

    // Second row
    stringBuilder.append(VERTICAL.glyph)
        .append(PADDING)
        .append(printColored(STUDENT_CHAR, studentsList.poll()))
        .append(PADDING);

    if (!studentsList.isEmpty()) {
      stringBuilder.append(printColored(STUDENT_CHAR, studentsList.poll()))
          .append(PADDING);
    } else {
      stringBuilder.append(PADDING_DOUBLE);
    }
    stringBuilder.append(VERTICAL.glyph)
        .append(System.lineSeparator());
    stringBuilder.append("╰─────╯");

    return stringBuilder.toString();
  }

  private String centredTitle(String title) {
    int rowLength = ("╭──I──╮" + PADDING_FROM_EACH_CLOUD).repeat(clouds.size()).length() - PADDING_FROM_EACH_CLOUD.length();
    int nPadding = (rowLength / 2) - (int) (Math.floor((double) title.length() / 2));

    return "-".repeat(nPadding) + title + "-".repeat(nPadding + 1);
  }
}
