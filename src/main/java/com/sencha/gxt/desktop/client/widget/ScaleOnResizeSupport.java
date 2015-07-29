package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * Provides support for scaling the position and size of a window manager's windows when the browser window's size
 * changes.
 */
public class ScaleOnResizeSupport {

  /**
   * Provides scaling support for windows that are minimized during browser window resizing and then later restored.
   * There is a 1:1 correspondence between ScaleOnResizeWindowHandler and Window.
   */
  private class ScalingWindowHandler implements BeforeHideHandler, ShowHandler {

    private int hideBrowserWidth;
    private int hideBrowserHeight;
    private int hideWindowLeft;
    private int hideWindowTop;

    @Override
    public void onBeforeHide(BeforeHideEvent event) {
      hideBrowserWidth = XDOM.getViewportWidth();
      hideBrowserHeight = XDOM.getViewportHeight();
      Window window = (Window) event.getSource();
      hideWindowLeft = window.getAbsoluteLeft();
      hideWindowTop = window.getAbsoluteTop();
    }

    @Override
    public void onShow(ShowEvent event) {
      if (scaleOnResize) {
        if (hideBrowserWidth != 0) { // i.e. not initial show before any hide
          int newBrowserWidth = XDOM.getViewportWidth();
          int newBrowserHeight = XDOM.getViewportHeight();
          if (newBrowserWidth != hideBrowserWidth || newBrowserHeight != hideBrowserHeight) {
            double horizontalScale = (double) newBrowserWidth / hideBrowserWidth;
            double verticalScale = (double) newBrowserHeight / hideBrowserHeight;
            Window window = (Window) event.getSource();
            scaleWindow(window, horizontalScale, verticalScale, hideWindowLeft, hideWindowTop);
          }
        }
      }
    }
  }

  private WindowManager windowManager;
  private int oldBrowserWidth;
  private int oldBrowserHeight;
  private boolean scaleOnResize = true;

  /**
   * Creates a new scale on resize support using the specified window manager to get a list of windows to scale.
   * 
   * @param windowManager manages the list of windows to scale
   */
  public ScaleOnResizeSupport(WindowManager windowManager) {
    this.windowManager = windowManager;
  }

  /**
   * Adds scaling support for a window that is minimized during browser window resizing and then later restored.
   * 
   * @param window the window to receive scaling support while minimized
   */
  public void addWindow(Window window) {
    ScalingWindowHandler swh = new ScalingWindowHandler();
    windowManager.addHandlerRegistration(window, window.addBeforeHideHandler(swh));
    windowManager.addHandlerRegistration(window, window.addShowHandler(swh));
  }

  /**
   * Initializes the scale on resize support.
   */
  public void initialize() {
    oldBrowserWidth = XDOM.getViewportWidth();
    oldBrowserHeight = XDOM.getViewportHeight();
    com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(ResizeEvent event) {
        onBrowserWindowResize();
      }
    });
  }

  /**
   * Returns true if the position and size of the window manager's windows are scaled when the browser's window size
   * changes.
   * 
   * @return true if the window manager's windows are scaled on a browser window resize
   */
  public boolean isScaleOnResize() {
    return scaleOnResize;
  }

  /**
   * Sets whether the position and size of the window manager's windows are scaled when the browser's window size
   * changes.
   * 
   * @param scaleOnResize true if window manager's windows are scaled on a browser window resize
   * 
   */
  public void setScaleOnResize(boolean scaleOnResize) {
    this.scaleOnResize = scaleOnResize;
  }

  /**
   * Handles a browser window resize... if the scale on resize flag is set, scales the position and size of the window
   * manager's windows based on the change in the browser window's size.
   */
  protected void onBrowserWindowResize() {
    int newBrowserWidth = XDOM.getViewportWidth();
    int newBrowserHeight = XDOM.getViewportHeight();
    if (scaleOnResize) {
      double horizontalScale = (double) newBrowserWidth / oldBrowserWidth;
      double verticalScale = (double) newBrowserHeight / oldBrowserHeight;
      for (Window window : windowManager.getWindows()) {
        if (window.isVisible()) {
          int oldWindowLeft = window.getAbsoluteLeft();
          int oldWindowTop = window.getAbsoluteTop();
          scaleWindow(window, horizontalScale, verticalScale, oldWindowLeft, oldWindowTop);
        }
      }
    }
    oldBrowserWidth = newBrowserWidth;
    oldBrowserHeight = newBrowserHeight;
  }

  private void scaleWindow(Window window, double horizontalScale, double verticalScale, int oldWindowLeft,
      int oldWindowTop) {
    int newWindowLeft = (int) (oldWindowLeft * horizontalScale);
    int newWindowTop = (int) (oldWindowTop * verticalScale);
    window.setPosition(newWindowLeft, newWindowTop);
    int oldWindowWidth = window.getOffsetWidth();
    int oldWindowHeight = window.getOffsetHeight();
    int newWindowWidth = (int) (oldWindowWidth * horizontalScale);
    int newWindowHeight = (int) (oldWindowHeight * verticalScale);
    window.setPixelSize(newWindowWidth, newWindowHeight);
  }

}
