package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.cell.core.client.form.ToggleButtonCell;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.desktop.client.theme.base.taskbutton.TaskButtonCellDefaultAppearance;

/**
 * Provides the unique appearance of a desktop task button cell. A cell is a
 * lightweight representation of a renderable object. A task button cell
 * inherits many of the properties of a toggle button cell.
 * <p/>
 * For more information on the use of the appearance pattern, see <a
 * href='http://www.sencha.com/blog/ext-gwt-3-appearance-design'>Sencha GXT 3.0
 * Appearance Design</a>
 */
public class TaskButtonCell extends ToggleButtonCell {

  /**
   * Defines the appearance interface for a task button cell.
   * <p/>
   * The appearance interface defines the interaction between the widget and an
   * appearance instance. The concrete implementation of the appearance
   * interface typically incorporates the external HTML and CSS source using the
   * {@link XTemplates} and {@link CssResource} interfaces.
   * 
   * @param <T> the type that this Cell represents
   */
  public interface TaskButtonCellAppearance<T> extends ButtonCellAppearance<T> {
  }

  /**
   * Constructs a task button cell with the default appearance.
   * <p/>
   * The GWT module file contains a replace-with directive that maps the
   * appearance interface (specified as the argument to the create method) to a
   * concrete implementation class, e.g. {@link TaskButtonCellDefaultAppearance}.
   * See {@code Desktop.gwt.xml} for more information.
   */
  public TaskButtonCell() {
    this(GWT.<TaskButtonCellAppearance<Boolean>> create(TaskButtonCellAppearance.class));
  }

  /**
   * Creates a task button cell with the specified appearance.
   * 
   * @param appearance the appearance of the task button cell
   */
  public TaskButtonCell(TaskButtonCellAppearance<Boolean> appearance) {
    super(appearance);
  }

}
