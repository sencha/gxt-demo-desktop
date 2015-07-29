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