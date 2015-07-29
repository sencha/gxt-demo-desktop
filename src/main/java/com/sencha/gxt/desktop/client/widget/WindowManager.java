package com.sencha.gxt.desktop.client.widget;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;

/**
 * Provides state management for a list of windows.
 */
public class WindowManager {

  private static class HandlerRegistrations {

    private List<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();

    private void addHandlerRegistration(HandlerRegistration handlerRegistration) {
      handlerRegistrations.add(handlerRegistration);
    }

    private void removeRegisteredHandlers() {
      for (HandlerRegistration handlerRegistration : handlerRegistrations) {
        handlerRegistration.removeHandler();
      }
      handlerRegistrations.clear();
    }

  }

  private class WindowDescriptor {

    private boolean isMinimized;
    private TaskButton taskButton;
    private HandlerRegistrations handlerRegistrations;

    private HandlerRegistrations getHandlerRegistrations() {
      if (handlerRegistrations == null) {
        handlerRegistrations = new HandlerRegistrations();
      }
      return handlerRegistrations;
    }

    private TaskButton getTaskButton() {
      return taskButton;
    }

    private boolean isMinimized() {
      return isMinimized;
    }

    private void setMinimized(boolean isMinimized) {
      this.isMinimized = isMinimized;
    }

    private void setTaskButton(TaskButton taskButton) {
      this.taskButton = taskButton;
    }

  }

  private Map<Window, WindowDescriptor> windows = new LinkedHashMap<Window, WindowDescriptor>();

  /**
   * Adds a window to the list of managed windows
   * 
   * @param window the window to add
   * @return true if the window was not already in the list
   */
  public boolean add(Window window) {
    boolean isAdded;
    if (contains(window)) {
      isAdded = false;
    } else {
      windows.put(window, new WindowDescriptor());
      isAdded = true;
    }
    return isAdded;
  }

  /**
   * Adds a handler registration for the specified window. The handler
   * registrations for a window may be removed using
   * {@link #removeRegisteredHandlers(Window)}.
   * 
   * @param window the window
   * @param handlerRegistration a handler association for the window
   * 
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public void addHandlerRegistration(Window window, HandlerRegistration handlerRegistration) {
    getWindowDescriptor(window).getHandlerRegistrations().addHandlerRegistration(handlerRegistration);
  }

  /**
   * Returns true if the specified window is in the list of managed windows.
   * 
   * @param window the window to check
   * @return true if the specified window is in the list of managed windows
   */
  public boolean contains(Window window) {
    return windows.containsKey(window);
  }

  /**
   * Returns the window that contains the specified widget as its top level
   * widget, or null if no window contains the widget.
   * 
   * @param widget the widget to search for
   * @return the window containing the widget as its top level wiget, or null if
   *         no windows contains the widget
   */
  public Window find(Widget widget) {
    for (Window window : getWindows()) {
      if (window.getWidget() == widget) {
        return window;
      }
    }
    return null;
  }

  /**
   * Returns the task button associated with the specified window.
   * 
   * @param window the window
   * @return the task button associated with the window
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public TaskButton getTaskButton(Window window) {
    return getWindowDescriptor(window).getTaskButton();
  }

  /**
   * Returns an iterable of managed windows.
   * 
   * @return an iterable of managed windows
   */
  public Iterable<Window> getWindows() {
    return windows.keySet();
  }

  /**
   * Returns true if the specified window is minimized.
   * 
   * @param window the window
   * @return true if the specified window is minimized.
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public boolean isMinimized(Window window) {
    return getWindowDescriptor(window).isMinimized();
  }

  /**
   * Removes the specified window from the list of managed windows.
   * 
   * @param window the window
   */
  public void remove(Window window) {
    windows.remove(window);
  }

  /**
   * Removes the handlers registered against the specified window.
   * 
   * @param window the window
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public void removeRegisteredHandlers(Window window) {
    getWindowDescriptor(window).getHandlerRegistrations().removeRegisteredHandlers();
  }

  /**
   * Sets the minimized state of the specified window.
   * 
   * @param window the window
   * @param isMinimized true if the window is minimized
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public void setMinimized(Window window, boolean isMinimized) {
    getWindowDescriptor(window).setMinimized(isMinimized);
  }

  /**
   * Sets the task button associated with the specified window.
   * 
   * @param window the window
   * @param taskButton the task button associated with the window
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  public void setTaskButton(Window window, TaskButton taskButton) {
    getWindowDescriptor(window).setTaskButton(taskButton);
  }

  /**
   * Returns the descriptor for the specified window.
   * 
   * @param window the window
   * @return the descriptor
   * @throws IllegalArgumentException if the window is not in the list of
   *           managed windows
   */
  private WindowDescriptor getWindowDescriptor(Window window) {
    WindowDescriptor windowDescriptor = windows.get(window);
    if (windowDescriptor == null) {
      throw new IllegalArgumentException("window is not in list of managed windows");
    }
    return windowDescriptor;
  }

}