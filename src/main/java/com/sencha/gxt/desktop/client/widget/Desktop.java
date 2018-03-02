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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.desktop.client.layout.CascadeDesktopLayout;
import com.sencha.gxt.desktop.client.layout.CenterDesktopLayout;
import com.sencha.gxt.desktop.client.layout.DesktopLayout;
import com.sencha.gxt.desktop.client.layout.DesktopLayout.RequestType;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktop.client.layout.TileDesktopLayout;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * A desktop represents a desktop like application which contains a task bar, start menu, and shortcuts.
 * <p/>
 * Rather than adding content directly to the root panel, content should be wrapped in windows. Windows can be opened
 * via shortcuts and the start menu.
 * 
 * @see TaskBar
 * @see StartMenu
 * @see Shortcut
 */
public class Desktop implements IsWidget {

  private class WindowHandler implements ActivateHandler<Window>, DeactivateHandler<Window>, MinimizeHandler,
      HideHandler, ShowHandler {

    @Override
    public void onActivate(ActivateEvent<Window> event) {
      markActive((Window) event.getSource());
    }

    @Override
    public void onDeactivate(DeactivateEvent<Window> event) {
      markInactive((Window) event.getSource());
    }

    @Override
    public void onHide(HideEvent event) {
      hideWindow((Window) event.getSource());
    }

    @Override
    public void onMinimize(MinimizeEvent event) {
      minimizeWindow((Window) event.getSource());
    }

    @Override
    public void onShow(ShowEvent event) {
      showWindow((Window) event.getSource());
    }

  }

  /**
   * The default desktop layout type.
   */
  public static final DesktopLayoutType DEFAULT_DESKTOP_LAYOUT_TYPE = DesktopLayoutType.CENTER;
  private DesktopLayoutContainer desktop;
  private TaskBar taskBar;
  private List<Shortcut> shortcuts;
  private WindowHandler handler;
  private Window activeWindow;
  private VerticalLayoutContainer desktopContainer;
  private Viewport desktopViewport;
  private DesktopLayout desktopLayout;
  private FastMap<DesktopLayout> desktopLayouts;
  private WindowManager windowManager;
  private ScaleOnResizeSupport scaleOnResizeSupport;

  /**
   * Creates a new Desktop window.
   */
  public Desktop() {
    getScaleOnResizeSupport().initialize();
  }

  /**
   * Activates a widget by wrapping it in a window if it is not already a window, adding it to the desktop if it's not
   * already on the desktop, restoring it from a minimized state if it's minimized or bringing it to the front if it's
   * hidden.
   * 
   * @param widget the widget to activate
   */
  public void activate(Widget widget) {
    Window window = getWidgetWindow(widget);
    if (!getWindowManager().contains(window)) {
      addWindow(window);
    }
    if (window != null && !window.isVisible()) {
      window.show();
    } else {
      window.toFront();
    }
  }

  /**
   * Adds a shortcut to the desktop.
   * 
   * @param shortcut the shortcut to add
   */
  public void addShortcut(Shortcut shortcut) {
    getShortcuts().add(shortcut);
    getDesktop().add(shortcut, new BoxLayoutData(new Margins(5)));
  }

  /**
   * Adds an item to the "main menu" area of the desktop start menu.
   * 
   * @param menuItem the item to add to the main menu area of the start menu
   */
  public void addStartMenuItem(StartMainMenuItem menuItem) {
    getTaskBar().getStartMenu().add(menuItem);
  }

  /**
   * Adds an item to the "tool menu" area of the desktop start menu.
   * 
   * @param startToolMenuItem the item to add to the tool menu area of the start menu
   */
  public void addToolMenuItem(StartToolMenuItem startToolMenuItem) {
    getTaskBar().getStartMenu().addTool(startToolMenuItem);
  }

  /**
   * Adds a separator item to the "tool menu" area of the desktop start menu.
   */
  public void addToolSeparator() {
    getTaskBar().getStartMenu().addToolSeparator();
  }

  /**
   * Adds a window to the desktop.
   * 
   * @param window the window to add
   */
  public void addWindow(Window window) {
    if (getWindowManager().add(window)) {
      getScaleOnResizeSupport().addWindow(window);
      window.setContainer(getDesktopContainer().getElement());
      addWindowHandler(window);
      // Note: callback via onShow
    }
  }

  @Override
  public Widget asWidget() {
    return getDesktopViewport();
  }

  /**
   * Returns the container of the "desktop", which is the area that contains the shortcuts (i.e. minus the task bar).
   * 
   * @return the desktop layout container
   */
  public DesktopLayoutContainer getDesktop() {
    if (desktop == null) {
      desktop = new DesktopLayoutContainer();
    }
    return desktop;
  }

  /**
   * Returns a list of the desktop's shortcuts.
   * 
   * @return the shortcuts
   */
  public List<Shortcut> getShortcuts() {
    if (shortcuts == null) {
      shortcuts = new ArrayList<Shortcut>();
    }
    return shortcuts;

  }

  /**
   * Returns the desktop's start menu.
   * 
   * @return the start menu
   */
  public StartMenu getStartMenu() {
    return taskBar.getStartMenu();
  }

  /**
   * Returns the desktop's task bar.
   * 
   * @return the task bar
   */
  public TaskBar getTaskBar() {
    if (taskBar == null) {
      taskBar = new TaskBar();
    }
    return taskBar;
  }

  /**
   * Returns a list of the desktop's windows.
   * 
   * @return the windows
   */
  public WindowManager getWindowManager() {
    if (windowManager == null) {
      windowManager = new WindowManager();
    }
    return windowManager;
  }

  /**
   * Returns true if the position and size of desktop windows are scaled when the browser's window size changes.
   * 
   * @return true if desktop windows are scaled on a browser window resize
   */
  public boolean isScaleOnResize() {
    return getScaleOnResizeSupport().isScaleOnResize();
  }

  /**
   * Arranges the windows on the desktop using the requested layout manager.
   * 
   * @param desktopLayoutType the type of layout manager to use
   */
  public void layout(DesktopLayoutType desktopLayoutType) {
    layout(getDesktopLayout(desktopLayoutType), null, RequestType.LAYOUT);
  }

  /**
   * Minimizes the window.
   * 
   * @param window the window to minimize
   */
  public void minimizeWindow(Window window) {
    getWindowManager().setMinimized(window, true);
    window.hide();
  }

  /**
   * Removes a shortcut from the desktop.
   * 
   * @param shortcut the shortcut to remove
   */
  public void removeShortcut(Shortcut shortcut) {
    getShortcuts().remove(shortcut);
    getDesktop().remove(shortcut);
  }

  /**
   * Removes a window from the desktop.
   * 
   * @param window the window to remove
   */
  public void removeWindow(Window window) {
    if (getWindowManager().contains(window)) {
      taskBar.removeTaskButton(getWindowManager().getTaskButton(window));
      getWindowManager().removeRegisteredHandlers(window);
      getWindowManager().remove(window);
      if (activeWindow == window) {
        activeWindow = null;
      }
    }
  }

  /**
   * Sets the type of layout manager to use when new windows are added to the desktop, or the desktop size changes.
   * 
   * @param desktopLayoutType the type of layout manager
   */
  public void setDesktopLayoutType(DesktopLayoutType desktopLayoutType) {
    desktopLayout = getDesktopLayout(desktopLayoutType);
  }

  /**
   * Sets whether the position and size of desktop windows are scaled when the browser's window size changes.
   * 
   * @param scaleOnResize true if desktop windows are scaled on a browser window resize
   * 
   */
  public void setScaleOnResize(boolean scaleOnResize) {
    getScaleOnResizeSupport().setScaleOnResize(scaleOnResize);
  }

  /**
   * Sets the desktop start menu heading text.
   * 
   * @param heading the heading text
   */
  public void setStartMenuHeading(String heading) {
    taskBar.getStartMenu().setHeading(heading);
  }

  /**
   * Sets the desktop start menu heading icon.
   * 
   * @param icon the heading icon
   */
  public void setStartMenuIcon(ImageResource icon) {
    taskBar.getStartMenu().setIcon(icon);
  }

  /**
   * Gets the scale on resize support. Override this method and extend ScaleOnResizeSupport to customize the scaling of desktop
   * windows when the browser's window size changes.
   * 
   * @return the scale on resize support, creating it if necessary
   */
  protected ScaleOnResizeSupport getScaleOnResizeSupport() {
    if (scaleOnResizeSupport == null) {
      scaleOnResizeSupport = new ScaleOnResizeSupport(getWindowManager());
    }
    return scaleOnResizeSupport;
  }

  private void addWindowHandler(Window window) {
    getWindowManager().addHandlerRegistration(window, window.addActivateHandler(getHandler()));
    getWindowManager().addHandlerRegistration(window, window.addDeactivateHandler(getHandler()));
    getWindowManager().addHandlerRegistration(window, window.addMinimizeHandler(getHandler()));
    getWindowManager().addHandlerRegistration(window, window.addHideHandler(getHandler()));
    getWindowManager().addHandlerRegistration(window, window.addShowHandler(getHandler()));
  }

  private DesktopLayout createDesktopLayout(DesktopLayoutType desktopLayoutType) {
    DesktopLayout desktopLayout;
    switch (desktopLayoutType) {
      case CASCADE:
        desktopLayout = new CascadeDesktopLayout();
        break;
      case CENTER:
        desktopLayout = new CenterDesktopLayout();
        break;
      case TILE:
        desktopLayout = new TileDesktopLayout();
        break;
      default:
        throw new IllegalArgumentException("Unsupported desktopLayoutType" + desktopLayoutType);
    }
    return desktopLayout;
  }

  private VerticalLayoutContainer getDesktopContainer() {
    if (desktopContainer == null) {
      desktopContainer = new VerticalLayoutContainer();
      desktopContainer.add(getDesktop(), new VerticalLayoutData(-1, 1));
      desktopContainer.add(getTaskBar(), new VerticalLayoutData(1, -1));
    }
    return desktopContainer;
  }

  private DesktopLayout getDesktopLayout() {
    if (desktopLayout == null) {
      desktopLayout = getDesktopLayout(DEFAULT_DESKTOP_LAYOUT_TYPE);
    }
    return desktopLayout;
  }

  private DesktopLayout getDesktopLayout(DesktopLayoutType desktopLayoutType) {
    DesktopLayout desktopLayout = getDesktopLayouts().get(desktopLayoutType.name());
    if (desktopLayout == null) {
      desktopLayout = createDesktopLayout(desktopLayoutType);
      getDesktopLayouts().put(desktopLayoutType.name(), desktopLayout);
    }
    return desktopLayout;
  }

  private FastMap<DesktopLayout> getDesktopLayouts() {
    if (desktopLayouts == null) {
      desktopLayouts = new FastMap<DesktopLayout>();
    }
    return desktopLayouts;
  }

  private Viewport getDesktopViewport() {
    if (desktopViewport == null) {
      desktopViewport = new Viewport();
      desktopViewport.add(getDesktopContainer());
    }
    return desktopViewport;
  }

  private WindowHandler getHandler() {
    if (handler == null) {
      handler = new WindowHandler();
    }
    return handler;
  }

  private Window getWidgetWindow(Widget widget) {
    Window window;
    if (widget instanceof Window) {
      window = (Window) widget;
    } else {
      window = getWindowManager().find(widget);
      if (widget == null) {
        window = new Window();
        window.add(widget);
      }
    }
    return window;
  }

  private void hideWindow(Window window) {
    if (getWindowManager().isMinimized(window)) {
      markInactive(window);
      return;
    }
    if (activeWindow == window) {
      activeWindow = null;
    }
    taskBar.removeTaskButton(getWindowManager().getTaskButton(window));
    windowManager.remove(window);
    layout(window, RequestType.HIDE);
  }

  private boolean isMaximized() {
    for (Window window : getWindowManager().getWindows()) {
      if (window.isVisible() && window.isMaximized()) {
        return true;
      }
    }
    return false;
  }

  private void layout(DesktopLayout desktopLayout, Window window, RequestType requestType) {
    if (!isMaximized()) {
      desktopLayout.layoutDesktop(window, requestType, getDesktop().getElement(), getWindowManager().getWindows(),
          getDesktop().getOffsetWidth(), getDesktop().getOffsetHeight());
    }
  }

  private void layout(Window window, RequestType requestType) {
    layout(getDesktopLayout(), window, requestType);
  }

  private void markActive(Window window) {
    if (activeWindow != null && activeWindow != window) {
      markInactive(activeWindow);
    }
    TaskButton taskButton = getWindowManager().getTaskButton(window);
    taskBar.setActiveButton(taskButton);
    activeWindow = window;
    taskButton.setValue(true);
    getWindowManager().setMinimized(window, false);
  }

  private void markInactive(Window window) {
    if (window == activeWindow) {
      activeWindow = null;
      TaskButton taskButton = getWindowManager().getTaskButton(window);
      taskButton.setValue(false);
    }
  }

  private void showWindow(Window window) {
    TaskButton taskButton = getWindowManager().getTaskButton(window);
    getWindowManager().setMinimized(window, false);
    if (taskButton != null && taskBar.getButtons().contains(taskButton)) {
      layout(window, RequestType.SHOW);
      return;
    }
    taskButton = taskBar.addTaskButton(window);
    getWindowManager().setTaskButton(window, taskButton);
    layout(window, RequestType.OPEN);
  }

}
