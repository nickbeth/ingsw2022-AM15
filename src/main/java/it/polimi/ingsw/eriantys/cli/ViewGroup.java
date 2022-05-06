package it.polimi.ingsw.eriantys.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A View which contains multiple views. When this view is drawn, it will call the draw method of each contained view.
 */
public class ViewGroup extends View {
  private final List<View> viewList = new LinkedList<>();

  /**
   * Adds a view to this group.
   *
   * @param view The view to add to this group.
   */
  public void addView(View view) {
    viewList.add(view);
  }

  /**
   * Removes a view from this group.
   *
   * @param view The view to remove from this group.
   * @return True if the view was removed, false otherwise.
   */
  public boolean removeView(View view) {
    return viewList.remove(view);
  }

  /**
   * Draws all views in this group.
   */
  public void draw(PrintStream o) {
    for (View view : viewList) {
      view.draw(o);
    }
  }
}
