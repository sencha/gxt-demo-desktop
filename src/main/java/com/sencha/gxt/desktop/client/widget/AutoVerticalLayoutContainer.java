package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class AutoVerticalLayoutContainer extends VerticalLayoutContainer {
  @Override
  public void add(Widget child) {
    super.add(child);
    doLayout();
  }
}