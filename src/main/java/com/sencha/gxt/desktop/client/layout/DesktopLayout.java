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
