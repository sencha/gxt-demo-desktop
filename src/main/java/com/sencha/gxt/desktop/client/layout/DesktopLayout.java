package com.sencha.gxt.desktop.client.layout;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Window;

public interface DesktopLayout {

  public enum RequestType {
    OPEN, HIDE, SHOW, LAYOUT
  }

  static final int PREFERRED_WIDTH = 400;
  static final int PREFERRED_HEIGHT = 400;
  static final int PREFERRED_MAX_WIDTH_PCT = 75;
  static final int PREFERRED_MAX_HEIGHT_PCT = 75;

  DesktopLayoutType getDesktopLayoutType();

  /**
   * Requests a layout of the desktop as indicated by the specified values.
   * 
   * @param requestWindow the window that was responsible for the request, or
   *          null if the request is not window specific
   * @param requestType the type of layout request
   * @param element the desktop element to be used for positioning
   * @param windows a list of all windows on the desktop
   * @param width the desktop width
   * @param height the desktop height
   */
  void layoutDesktop(Window requestWindow, RequestType requestType, Element element, Iterable<Window> windows,
      int width, int height);

}
