/**
 * Sencha GXT 1.0.0-SNAPSHOT - Sencha for GWT
 * Copyright (c) 2006-2018, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Commercial License
 * ================================================================================
 * This version of Sencha GXT is licensed commercially and is the appropriate
 * option for the vast majority of use cases.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 *
 *
 *
 *
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.desktop.client.widget;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * Displays the start menu button followed by a list of open windows.
 */
public class TaskBar extends ToolBar {

  /**
   * Provides access to task bar messages.
   */
  public interface TaskBarMessages {

    /**
     * Returns the task bar start button text.
     * 
     * @return the task bar start button text
     */
    String startButtonText();

  }

  protected class DefaultTaskBarMessages implements TaskBarMessages {

    public String startButtonText() {
      return DefaultMessages.getMessages().desktop_startButton();
    }

  }

  private TaskBarMessages messages;
  private String startButtonText;
  private StartButton startButton;
  private int buttonWidth = 168;
  private int minButtonWidth = 118;
  private boolean resizeButtons = true;

  /**
   * Creates a task bar.
   */
  public TaskBar() {
    setHeight(30);
    addStyleName("x-taskbar");
    setSpacing(-1);
    getElement().getStyle().setProperty("border", "none");
    setPadding(new Padding(0));

    startButton = new StartButton();
    startButton.setText(getStartButtonText());
    add(startButton, new BoxLayoutData(new Margins(0, 4, 0, 0)));
  }

  /**
   * Adds a button.
   * 
   * @param win the window
   * @return the new task button
   */
  public TaskButton addTaskButton(Window win) {
    TaskButton taskButton = new TaskButton(win);
    add(taskButton, new BoxLayoutData(new Margins(0, 3, 0, 0)));
    autoSize();
    doLayout();
    setActiveButton(taskButton);
    return taskButton;
  }

  /**
   * Returns the bar's buttons.
   * 
   * @return the buttons
   */
  public List<TaskButton> getButtons() {
    List<TaskButton> buttons = new LinkedList<TaskButton>();
    for (Widget widget : getChildren()) {
      if (widget instanceof TaskButton) {
        buttons.add((TaskButton) widget);
      }
    }
    return buttons;
  }

  /**
   * Returns the task bar messages.
   * 
   * @return the task bar messages
   */
  public TaskBarMessages getMessages() {
    if (messages == null) {
      messages = new DefaultTaskBarMessages();
    }
    return messages;
  }

  /**
   * Returns the start button text.
   * 
   * @return the start button text
   */
  public String getStartButtonText() {
    if (startButtonText == null) {
      startButtonText = getMessages().startButtonText();
    }
    return startButtonText;
  }

  /**
   * Returns the bar's start menu.
   * 
   * @return the start menu
   */
  public StartMenu getStartMenu() {
    return startButton.getStartMenu();
  }

  /**
   * Removes a button.
   * 
   * @param btn the button to remove
   */
  public void removeTaskButton(TaskButton btn) {
    remove(btn);
    autoSize();
    doLayout();
  }

  /**
   * Sets the active button.
   * 
   * @param btn the button
   */
  public void setActiveButton(TaskButton btn) {
    // TODO: Provide implementation, v2 did not provide full support
  }

  /**
   * Sets the task bar messages.
   * 
   * @param messages the messages
   */
  public void setMessages(TaskBarMessages messages) {
    this.messages = messages;
    if (startButtonText != null) {
      setStartButtonText(messages.startButtonText());
    }
  }

  /**
   * Sets the start button text (defaults to 'Start').
   * 
   * @param startButtonText the start button text
   */
  public void setStartButtonText(String startButtonText) {
    this.startButtonText = startButtonText;
    startButton.setText(startButtonText);
  }

  private void autoSize() {
    List<TaskButton> buttons = getButtons();
    int count = buttons.size();
    int aw = getOffsetWidth();

    if (!resizeButtons || count < 1) {
      return;
    }

    int each = (int) Math.max(Math.min(Math.floor((aw - 4) / count), buttonWidth), minButtonWidth);

    for (TaskButton taskButton : buttons) {
      taskButton.setWidth(each);
    }
  }

}
