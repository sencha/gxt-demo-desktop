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

public class CascadeDesktopLayout extends LimitedDesktopLayout implements DesktopLayout {

  protected int left;
  protected int top;
  protected int offsetIndex;

  @Override
  public DesktopLayoutType getDesktopLayoutType() {
    return DesktopLayoutType.CASCADE;
  }

  @Override
  public void layoutDesktop(Window requestWindow, RequestType requestType, Element element, Iterable<Window> windows,
      int containerWidth, int containerHeight) {

    if (requestType == RequestType.LAYOUT) {
      left = 0;
      top = 0;
      offsetIndex = 0;
    }

    super.layoutDesktop(requestWindow, requestType, element, windows, containerWidth, containerHeight);
  }

  @Override
  protected void layoutWindow(Window window, int containerWidth, int containerHeight, int width, int height) {

    int offset = window.getHeader().getOffsetHeight();

    window.setPixelSize(width, height);

    if (top + height > containerHeight) {
      left = ++offsetIndex * offset;
      top = 0;
    }

    if (left + width > containerWidth) {
      left = 0;
      top = 0;
      offsetIndex = 0;
    }

    window.setPosition(left, top);

    left += offset;
    top += offset;
  }

}
