package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.desktop.client.theme.base.startmenu.StartToolMenuItemAppearance;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Provides a "tool menu" item for a start menu. A tool menu item is displayed
 * in the tool menu of the start menu, which is typically to the right of the
 * main menu. In it's current implementation, a tool menu item has a unique
 * appearance that is appropriate for the tool menu.
 * 
 * @see StartToolMenu
 */
public class StartToolMenuItem extends MenuItem {

  /**
   * Creates a tool menu item for the start menu, with the default tool menu
   * item appearance.
   */
  public StartToolMenuItem() {
    this(new StartToolMenuItemAppearance());
  }

  /**
   * Creates a tool menu item for the start menu, with the specified tool menu
   * item appearance.
   * 
   * @param startToolMenuItemAppearance the appearance
   */
  public StartToolMenuItem(StartToolMenuItemAppearance startToolMenuItemAppearance) {
    super(startToolMenuItemAppearance);
  }

  /**
   * Creates a tool menu item for the start menu with the default appearance and
   * the specified text.
   * 
   * @param text the tool menu item text
   */
  public StartToolMenuItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a tool menu item for the start menu with the default appearance and
   * the specified text and icon.
   * 
   * @param text the tool menu item text
   * @param icon the icon
   */
  public StartToolMenuItem(String text, ImageResource icon) {
    this(text);
    setIcon(icon);
  }

  /**
   * Hides a displayed sub-menu, if any.
   */
  void hideSubMenu() {
    super.deactivate();
  }
}