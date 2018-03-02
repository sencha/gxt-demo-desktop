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
package com.sencha.gxt.desktop.client.layout;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.desktop.client.layout.DesktopLayout.RequestType;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.WindowManager;

public abstract class LimitedDesktopLayout {

  public void layoutDesktop(Window requestWindow, RequestType requestType, Element element, Iterable<Window> windows,
      int containerWidth, int containerHeight) {

    int maxWidth = getPercent(containerWidth, DesktopLayout.PREFERRED_MAX_WIDTH_PCT);
    int maxHeight = getPercent(containerHeight, DesktopLayout.PREFERRED_MAX_HEIGHT_PCT);

    int width = DesktopLayout.PREFERRED_WIDTH;
    int height = DesktopLayout.PREFERRED_HEIGHT;

    width = Math.min(width, maxWidth);
    height = Math.min(height, maxHeight);

    switch (requestType) {
      case HIDE:
      case SHOW:
        // do nothing
        break;
      case LAYOUT:
        for (Window window : windows) {
          layoutWindow(window, containerWidth, containerHeight, width, height);
          // TODO: Determine why z-order is still messed up on cascade
          WindowManager.get().bringToFront(window);
        }
        break;
      case OPEN:
        layoutWindow(requestWindow, containerWidth, containerHeight, width, height);
        break;
    }

  }

  protected abstract void layoutWindow(Window window, int containerWidth, int containerHeight, int width, int height);

  private int getPercent(int value, int percent) {
    return (value * percent) / 100;
  }

}