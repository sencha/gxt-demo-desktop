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
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.Window;

public class TileDesktopLayout implements DesktopLayout {

  private static final int MARGIN_BOTTOM = 10;
  private static final int MARGIN_LEFT = 10;
  private static final int MARGIN_RIGHT = 10;
  private static final int MARGIN_TOP = 10;
  private static final int SPACING = 10;

  @Override
  public DesktopLayoutType getDesktopLayoutType() {
    return DesktopLayoutType.TILE;
  }

  @Override
  public void layoutDesktop(Window requestWindow, RequestType requestType, Element element, Iterable<Window> windows,
      int width, int height) {

    int layoutCount = 0;

    for (Window window : windows) {
      if (window.isVisible()) {
        layoutCount++;
      }
    }

    int availableWidth = width - (MARGIN_LEFT + MARGIN_RIGHT);
    int availableHeight = height - (MARGIN_TOP + MARGIN_BOTTOM);

    int rowCount = 1;
    int columnCount = 1;
    int bestFit = Integer.MAX_VALUE;

    for (int testRowCount = 1; testRowCount <= layoutCount; testRowCount++) {
      int testColumnCount = (layoutCount + (testRowCount - 1)) / testRowCount;
      int tileWidth = availableWidth / testColumnCount;
      int tileHeight = availableHeight / testRowCount;
      int delta = tileWidth - tileHeight;
      if (delta < 0) {
        delta = -delta;
      }
      if (delta < bestFit) {
        bestFit = delta;
        rowCount = testRowCount;
        columnCount = testColumnCount;
      }
    }

    int horizontalSpacing = (columnCount - 1) * SPACING;
    int verticalSpacing = (rowCount - 1) * SPACING;

    int tileWidth = (availableWidth - horizontalSpacing) / columnCount;
    int tileHeight = (availableHeight - verticalSpacing) / rowCount;

    int layoutIndex = 0;

    for (Window window : windows) {
      if (window.isVisible()) {
        int row = layoutIndex / columnCount;
        int column = layoutIndex % columnCount;
        int top = MARGIN_TOP + (row * tileHeight + row * SPACING);
        int left = MARGIN_LEFT + (column * tileWidth + column * SPACING);
        window.setPixelSize(tileWidth, tileHeight); // must set prior to alignTo
        window.alignTo(element, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_LEFT), left, top);
        layoutIndex++;
      }
    }
  }

}
