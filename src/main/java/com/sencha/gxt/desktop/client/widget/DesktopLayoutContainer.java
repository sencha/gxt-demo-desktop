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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;

/**
 * Arranges widgets in a top-down, left-to-right manner. The container is
 * divided into a grid of equal sized cells based on the width and height of the
 * largest widget. Widgets are then positioned in these cells based on the
 * settings of {@link HBoxLayoutAlign} and {@link VBoxLayoutAlign} to produce a
 * series of columns and rows. Widgets are arranged in each column in a top-down
 * order, starting with the left-most column and proceeding to the right.
 */
public class DesktopLayoutContainer extends InsertResizeContainer {

  public interface DesktopLayoutContainerAppearance {
    public XElement getShortcutArea(XElement parent);

    public void render(SafeHtmlBuilder sb);
  }

  private HBoxLayoutAlign hBoxLayoutAlign;
  private VBoxLayoutAlign vBoxLayoutAlign;
  private DesktopLayoutContainerAppearance appearance;

  /**
   * Creates a desktop layout container with the default appearance.
   */
  public DesktopLayoutContainer() {
    this(GWT.<DesktopLayoutContainerAppearance> create(DesktopLayoutContainerAppearance.class));
  }

  /**
   * Creates a desktop layout container with the specified appearance.
   * 
   * @param appearance the desktop layout container appearance
   */
  public DesktopLayoutContainer(DesktopLayoutContainerAppearance appearance) {
    this.appearance = appearance;
    forceLayoutOnResize = true;
    setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
    setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);
    setElement((Element)XDOM.create(sb.toSafeHtml()));
  }

  /**
   * Adds a widget to this desktop layout container with the specified layout
   * parameters.
   * 
   * @param child the widget to add
   * @param layoutData the layout parameters
   */
  @UiChild(tagname = "child")
  public void add(IsWidget child, MarginData layoutData) {
    if (child != null) {
      child.asWidget().setLayoutData(layoutData);
    }
    add(child);
  }

  /**
   * Returns the horizontal alignment.
   * 
   * @return the horizontal alignment
   */
  public HBoxLayoutAlign getHBoxLayoutAlign() {
    return hBoxLayoutAlign;
  }

  /**
   * Returns the vertical alignment.
   * 
   * @return the vertical alignment
   */
  public VBoxLayoutAlign getVBoxLayoutAlign() {
    return vBoxLayoutAlign;
  }

  /**
   * Sets the horizontal alignment for child items (defaults to
   * {@link HBoxLayoutAlign#MIDDLE}).
   * 
   * @param hBoxLayoutAlign the horizontal alignment
   */
  public void setHBoxLayoutAlign(HBoxLayoutAlign hBoxLayoutAlign) {
    this.hBoxLayoutAlign = hBoxLayoutAlign;
  }

  /**
   * Sets the vertical alignment for child items (defaults to
   * {@link VBoxLayoutAlign#CENTER}).
   * 
   * @param vBoxLayoutAlign the vertical alignment
   */
  public void setVBoxLayoutAlign(VBoxLayoutAlign vBoxLayoutAlign) {
    this.vBoxLayoutAlign = vBoxLayoutAlign;
  }

  @Override
  protected void doLayout() {

    int widgetCount = getWidgetCount();
    if (widgetCount == 0) {
      return;
    }

    int maxWidth = 0;
    int maxHeight = 0;

    for (int i = 0; i < widgetCount; i++) {
      Widget widget = getWidget(i);
      Margins margins = getMargins(widget);
      maxWidth = Math.max(maxWidth, widget.getOffsetWidth() + margins.getLeft() + margins.getRight());
      maxHeight = Math.max(maxHeight, widget.getOffsetHeight() + margins.getTop() + margins.getBottom());
    }

    XElement element = getElement();
    Size size = XElement.as(element).getStyleSize();

    int availableHeight = size.getHeight() - XDOM.getScrollBarWidth();
    int rowCount = availableHeight / maxHeight;
    int columnCount = (widgetCount + (rowCount - 1)) / rowCount;
    int requiredWidth = columnCount * maxWidth;

    getContainerTarget().setSize(requiredWidth, availableHeight);

    for (int i = 0; i < widgetCount; i++) {

      Widget widget = getWidget(i);
      Margins margins = getMargins(widget);
      int row = i % rowCount;
      int column = i / rowCount;
      int left = column * maxWidth;
      int top = row * maxHeight;
      int widgetWidth = widget.getOffsetWidth();
      int widgetHeight = widget.getOffsetHeight();
      int marginWidth = margins.getLeft() + margins.getRight();
      int marginHeight = margins.getTop() + margins.getBottom();
      int excessWidth = maxWidth - widgetWidth - marginWidth;
      int excessHeight = maxHeight - widgetHeight - marginHeight;

      boolean isSizeChange = false;

      switch (vBoxLayoutAlign) {
        case CENTER:
          left += excessWidth / 2;
          break;
        case LEFT:
          // no action required
          break;
        case RIGHT:
          left += excessWidth;
          break;
        case STRETCH:
        case STRETCHMAX:
        default:
          widgetWidth = maxWidth - marginWidth;
          isSizeChange = true;
          break;
      }

      switch (hBoxLayoutAlign) {
        case TOP:
          // no action required
          break;
        case MIDDLE:
          top += excessHeight / 2;
          break;
        case BOTTOM:
          top += excessHeight;
          break;
        case STRETCH:
        case STRETCHMAX:
        default:
          widgetHeight = maxHeight - marginHeight;
          isSizeChange = true;
          break;
      }

      XElement.as(widget.getElement()).makePositionable(true);
      XElement.as(widget.getElement()).setLeftTop(left, top);

      if (isSizeChange) {
        applyLayout(widget, widgetWidth, widgetHeight);
      }
    }
  }

  protected XElement getContainerTarget() {
    return appearance.getShortcutArea(getElement());
  }

  private Margins getMargins(Widget widget) {
    Margins margins = null;
    Object layoutData = widget.getLayoutData();
    if (layoutData instanceof MarginData) {
      MarginData marginData = (MarginData) layoutData;
      margins = marginData.getMargins();
    }
    return margins == null ? new Margins(0) : margins;
  }

}
