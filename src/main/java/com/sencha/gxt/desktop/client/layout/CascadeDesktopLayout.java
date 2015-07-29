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
