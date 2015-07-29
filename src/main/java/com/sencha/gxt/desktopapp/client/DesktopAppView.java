package com.sencha.gxt.desktopapp.client;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;

/**
 * Provides a desktop application view.
 */
public interface DesktopAppView extends IsWidget, HasWidgets {

  /**
   * Sets the type of desktop layout to use when opening subsequent windows.
   * 
   * @param desktopLayoutType the layout to use when opening subsequent windows
   */
  void setDesktopLayoutType(DesktopLayoutType desktopLayoutType);

  /**
   * Sets whether the desktop should scale the position and size of desktop windows when the browser window is resized.
   * 
   * @param isScaleOnResize true if the desktop should scale desktop windows when the browser window is resized
   */
  void setScaleOnResize(boolean scaleOnResize);

}
