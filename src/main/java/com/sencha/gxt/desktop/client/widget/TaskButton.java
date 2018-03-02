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

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Provides a task button that can be added to a task bar and indicates the
 * current state of a desktop application. Clicking on a task button activates
 * the window associated with a desktop application. The active desktop
 * application is indicated by the "pressed" visual state of the task button.
 * 
 * @see TaskBar
 */
public class TaskButton extends ToggleButton {

  private Window win;

  /**
   * Creates a task button for the specified window.
   * 
   * @param win a window containing a desktop application
   */
  public TaskButton(Window win) {
    super(new TaskButtonCell());
    Header header = win.getHeader();
    ImageResource icon = header.getIcon();
    String text = header.getText();
    if (text != null) {
      setText(Format.ellipse(text, 26));
    }
    setIcon(icon);
    setHeight(28);
    this.win = win;
    addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        doSelect(event);
      }
    });
  }

  protected void doSelect(SelectEvent event) {
    if (!win.isVisible()) {
      win.show();
    } else if (win == WindowManager.get().getActive()) {
      win.minimize();
    } else {
      win.toFront();
    }
  }
}
